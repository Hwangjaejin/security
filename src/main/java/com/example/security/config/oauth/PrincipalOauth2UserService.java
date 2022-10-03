package com.example.security.config.oauth;

import com.example.security.config.CustomBCryptPasswordEncoder;
import com.example.security.config.auth.PrincipalDetails;
import com.example.security.model.User;
import com.example.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private CustomBCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    /**
     * 구글로그인 후 구글로부터 받은 userRequest 데이터에 대한 후처리가 되는 메서드
     * 오버라이드 하지 않아도 실행되지만 로그인 후 자동 회원가입을 하기위해
     * Authentication 객체에 UserDetails, OAuth2User 타입을 모두 구현한 PrincipalDetails를 넣기위해
     * 오버라이드함.
     */
    // 메서드 종료 시 @AuthenticationPrincipal 어노테이션이 만들어짐.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("==========OAuth로그인==========");

        System.out.println("getClientRegistration = " + userRequest.getClientRegistration()); // registrationId로 어떤 oauth로 로그인 했는지 확인가능.
        System.out.println("getRegistrationId = " + userRequest.getClientRegistration().getRegistrationId());
        System.out.println("getAccessToken = " + userRequest.getAccessToken());
        System.out.println("getTokenValue = " + userRequest.getAccessToken().getTokenValue());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 구글로그인 버튼 클릭 -> 구글로그인 창 -> 로그인 완료 -> code리턴(oauth2-client 라이브러리가 받음) -> Access Token요청
        // userRequest 정보 -> loadUser함수 호출 -> 구글로부터 회원프로필 리턴
        System.out.println("attributes = " + oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getClientId(); // google
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider + "_" + providerId;
        String password = bCryptPasswordEncoder.encode("겟인데어");
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);

        if (userEntity == null) {
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        } else {
            System.out.println("이미 가입한 회원입니다.");
        }

        return new PrincipalDetails(userEntity, oAuth2User.getAttributes()); // Authentication 객체로 리턴됨.
    }
}
