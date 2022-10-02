package com.example.security.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    // 구글로그인 후 구글로부터 받은 userRequest 데이터에 대한 후처리가 되는 메서드
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("getClientRegistration = " + userRequest.getClientRegistration());
        System.out.println("getAccessToken = " + userRequest.getAccessToken());
        System.out.println("getTokenValue = " + userRequest.getAccessToken().getTokenValue());
        System.out.println("getRegistrationId = " + userRequest.getClientRegistration().getRegistrationId());
        System.out.println("super.loadUser(userRequest) = " + super.loadUser(userRequest));
        return super.loadUser(userRequest);
    }
}