package com.test.service1.config;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;

@EnableWebSecurity // Enable Spring Security features
@EnableGlobalMethodSecurity(prePostEnabled = true) // Enable support / interpretation of security annotations, e.g. `@PreAuthorize(...)`
public class OAuth2LoginSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                .antMatchers("/public/**").permitAll() // Allow certain endpoints to go unauthenticated
                .anyRequest().authenticated()) // The other endpoints have to be authenticated
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    }
}
