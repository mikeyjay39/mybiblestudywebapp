package com.mybiblestudywebapp.persistenceservice.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 3/26/20
 */
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private static final String[] PUBLIC_URLS = {
            "/persistence/getStudyNotesForChapter/**",
            "/getChapter/**"
    };

    @Override
    public void configure(HttpSecurity http) throws Exception{
        http
                .authorizeRequests()
                .antMatchers(PUBLIC_URLS).permitAll()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated();
    }
}
