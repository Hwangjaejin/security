package com.example.security.config.auth;

import com.example.security.model.User;
import com.example.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

// 시큐리티 설정에서 loginProcessingUrl("/login"); 설정을 했기 때문에
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어 있는 빈에서 loadUserByUsername 메서드가 실행.
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username = " + username);

        User userEntity = userRepository.findByUsername(username);
        if (StringUtils.hasText(username)) {
            return new PrincipalDetails(userEntity); // Authentication 객체로 리턴됨. Authentication은 시큐리티 Session으로 반환됨.
        }

        return null;
    }
}
