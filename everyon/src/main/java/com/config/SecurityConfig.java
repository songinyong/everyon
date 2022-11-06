package com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.config.auth.FirebaseTokenFilter;
import com.google.firebase.auth.FirebaseAuth;
import com.service.CustomUserService;

import lombok.RequiredArgsConstructor;
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserService userDetailsService;
    
    @Autowired
    private FirebaseAuth firebaseAuth;   

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and().authorizeRequests()
                .anyRequest().authenticated().and()
                .addFilterBefore(new FirebaseTokenFilter(userDetailsService, firebaseAuth),
                     UsernamePasswordAuthenticationFilter.class);
}
}