package com.mybiblestudywebapp.authenticationservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;
import java.security.SecureRandom;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 3/21/20
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /*@Bean//(name = "webUserDetails")
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager();
        userDetailsManager.setDataSource(dataSource);
        return userDetailsManager;
    }*/

    @Bean
    public PasswordEncoder encoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/token")
                .permitAll().
                anyRequest().authenticated();
    }

/*
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("bob").password("pass1").roles("USER")
                .and()
                .withUser("will").password("pass2").roles("USER", "ADMIN")
        .and()
        .passwordEncoder(encoder());

    }*/

    @Autowired
    private DataSource dataSource;

    /*private static final String SALT = "thisisasalt";

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(12, new SecureRandom(SALT.getBytes()));
    }*/

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
}
