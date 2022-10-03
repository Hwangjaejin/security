package com.example.security.config.auth;

// 시큐리티가 /login 호출을 낚아채서 로그인을 진행시킨다.
// 로그인 진행이 완료되면 시큐리티 session을 만들어준다. (Security ContextHolder)
// 오브젝트 타입 => Authentication 타입 객체여야함.
// Authentication 안에 User 정보가 있어야 됨.
// User 오브젝트 타입 => UserDetails 타입 객체

// Security Session 영역에 저장될 수 있는 객체는 Authentication 객체로 정해져 있다.
// Authentication 객체안에 User정보를 저장할 때는 UserDetails 타입이어야 함.

import com.example.security.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * 스프링 시큐리티는 자기만의 세션이 있다.(시큐리티 세션)
 *
 * 시큐리티 세션에 들어갈 수 있는 타입은 Authentication 객체밖에 없다.
 *
 * Authentication 객체에는 2개의 타입만 들어갈 수 있다.
 * 1. UserDetails - 일반적인 로그인을 할 때 UserDetails 타입이 Authentication 객체로 들어가고
 * 2. OAuth2User - OAuth 로그인을 할 때 OAuth2User 타입이 Authentication 객체로 들어간다.
 *
 * 두 가지 모두를 구현한 클래스를 만들어서 사용.
 */
@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user; // Composition
    private Map<String, Object> attributes;

    // 일반 로그인할 때 사용되는 생성자
    public PrincipalDetails(User user) {
        this.user = user;
    }

    // OAuth 로그인할 때 사용되는 생성자
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    // 해당 User의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }
}
