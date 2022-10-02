package com.example.security.config;

import com.example.security.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// 1. Authorization 코드받기(인증), 2. Access Token 받기(권한)
// 3. 사용자프로필 정보, 4-1. 자동 회원가입
// 4-2. 구글 정보 + 회원가입 페이지를 통해 추가 정보 획득
@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터(SecurityConfig)가 스프링 필터체인(스프링 기본필터 체인)에 등록이 된다.
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true) // @Secured 활성화, @PreAuthorize/@PostAuthorize 활성화
public class SecurityConfig {

    @Autowired
    PrincipalOauth2UserService principalOauth2UserService;

    // 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/user/**").authenticated() // 인증만 되면 들어갈 수 있는 주소
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/loginForm") // formLogin() + loginPage() => 권한이 없는 경로는 /loginForm 페이지로 떨어지게함.
                .loginProcessingUrl("/login") // login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행한다.(Controller에 /login을 만들지 않아도 됨)
                .defaultSuccessUrl("/") // /loginForm 호출로 로그인을 하면 / 로 보내줌. 특정페이지(/user)를 호출해서 로그인을 하면 호출한 페이지로 보내줌.
                .and()
                .oauth2Login()
                .loginPage("/loginForm") // formLogin() + loginPage() <=> oauth2Login() + loginPage()
                .userInfoEndpoint()
                .userService(principalOauth2UserService); // AccessToken + 사용자 프로필정보를 PrincipalOauth2UserService의 userRequest로 넘김

        return http.build();
    }
}
