package com.example.security.config.oauth.provider;

public interface OAuth2UserInfo {
    String getProviderId(); // 구글과 페이스북이 id를 받는 key(구글 : sub, 페이스북 : id)가 다르기 때문에 interface를 사용
    String getProvider();
    String getEmail();
    String getName();
}
