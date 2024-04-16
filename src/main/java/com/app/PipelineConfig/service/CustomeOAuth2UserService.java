package com.app.PipelineConfig.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import com.app.PipelineConfig.OAuth2User.CustomeOAuth2User;

@Service
public class CustomeOAuth2UserService extends DefaultOAuth2UserService  {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user =  super.loadUser(userRequest);
        return new CustomeOAuth2User(user);
    }

    //@Override
    protected OAuth2User mapAuthorities(OAuth2User user, OAuth2UserRequest userRequest) {
        String sub = user.getAttribute("id"); // Получаем идентификатор пользователя из атрибута 'sub'
        return new DefaultOAuth2User(user.getAuthorities(), user.getAttributes(), "id"); // Возвращаем пользователя с атрибутом 'sub'
    }
}