/*
 * 로그인 보안 설정 담당
 * */

package com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
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
         /*.and()
        .authorizeRequests().
        anyRequest().permitAll();*/
    }
    
    //토큰 검증에서 제외대상
    @Override
    public void configure(WebSecurity web) throws Exception {
        /** 회원가입, 리소스
         * 회원가입이후에 다른 
         */
			web.ignoring().antMatchers(HttpMethod.POST, "/users/register")
			.antMatchers(HttpMethod.GET, "/")
            .antMatchers("/images/**")
            .antMatchers("/assets/**")
            .antMatchers("/*.html")
            .antMatchers("/resources/**");
    }
}