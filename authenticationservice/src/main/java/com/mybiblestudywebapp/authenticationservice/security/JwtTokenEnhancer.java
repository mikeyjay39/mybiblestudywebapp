package com.mybiblestudywebapp.authenticationservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 3/28/20
 */
public class JwtTokenEnhancer implements TokenEnhancer {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {

        long userId = getUserId(oAuth2Authentication.getName());
        Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("userId", userId);
        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(additionalInfo);
        return oAuth2AccessToken;
    }

    private long getUserId(String username) {

        String sql = "SELECT user_id FROM users WHERE email = ?;";
        long userId = jdbcTemplate.queryForObject(sql, new Object[]{username}, Long.class);
        return userId;
    }

}
