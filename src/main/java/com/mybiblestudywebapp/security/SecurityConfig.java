package com.mybiblestudywebapp.security;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

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
            "/biblestudy/**",
            "/test/**",
            "/users/**"
    };

    @Autowired
    private DataSource dataSource;

    private static final String SALT = "thisisasalt";

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
                ;
    }

    @Bean
    public PasswordEncoder encoder() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12, new SecureRandom(SALT.getBytes()));
        return encoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //http.addFilterBefore(characterEncodingFilter(), CsrfFilter.class);

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
        ;

        // CSRF tokens handling
        //http.addFilterAfter(new CsrfTokenResponseHeaderBindingFilter(), CsrfFilter.class);


        /*.and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);*/
        /*.and()
        .formLogin()
        .and()
        .httpBasic()
        .disable();*/

        /*http
                .cors()
                .and()
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/**").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/**").hasRole("USER")
                .antMatchers(HttpMethod.PUT, "/**").hasRole("USER")
                .antMatchers(HttpMethod.PATCH, "/**").hasRole("USER")
                .antMatchers(HttpMethod.DELETE, "/**").hasRole("USER")
                .antMatchers(HttpMethod.OPTIONS, "/**").hasRole("USER")
                .and()
                .csrf().disable()
                .formLogin().disable();*/

                /*.csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/**").authenticated()
                .and()
                .formLogin()
                .successHandler(mySuccessHandler)
                .failureHandler(myFailureHandler)
                .and()
                .logout();*/
    }

}
