package com.mybiblestudywebapp.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.sql.DataSource;
import java.security.SecureRandom;

import static org.springframework.http.HttpStatus.FORBIDDEN;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/25/19
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] PUBLIC_URLS = {
            "/index.html",
            "/dashboard.html",
            "/biblestudy/**",
            "/test/**",
            "/users/**",
            "/login/**",
            "/js/main.js"
    };

    @Autowired
    private DataSource dataSource;

    private static final String SALT = "thisisasalt";

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(12, new SecureRandom(SALT.getBytes()));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(
                        "SELECT email, password, enabled FROM users " +
                                "WHERE email = ?"
                )
                .authoritiesByUsernameQuery(
                        "SELECT email, authority FROM user_authorities " +
                                "WHERE email = ?"
                )
                .passwordEncoder(encoder())
                ;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .httpBasic().authenticationEntryPoint(new HttpStatusEntryPoint(FORBIDDEN))
                .and()
                .authorizeRequests()
                .antMatchers(PUBLIC_URLS)
                .permitAll()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // use this if we want to enable csrf protection
                .and()
                .headers()
                /*.contentSecurityPolicy("script-src 'self' https://trustedscripts.example.com; " +
                        "object-src https://trustedplugins.example.com; report-uri /csp-report-endpoint/")
                .and()*/
                .and()
                .logout()
                .logoutUrl("/perform_logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
        ;
    }
}
