package com.config.auth;

import java.io.IOException;
import java.util.NoSuchElementException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.config.auth.util.RequestUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.service.CustomUserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FirebaseTokenFilter extends OncePerRequestFilter{

    private CustomUserService userDetailsService;
    private FirebaseAuth firebaseAuth;

    public FirebaseTokenFilter(CustomUserService userDetailsService, FirebaseAuth firebaseAuth) {
        this.userDetailsService = userDetailsService;
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        // get the token from the request
        FirebaseToken decodedToken;
        try{
            String header = RequestUtil.getAuthorizationToken(request.getHeader("Authorization"));
            decodedToken = firebaseAuth.verifyIdToken(header);
        } catch (NullPointerException | FirebaseAuthException | IllegalArgumentException e) {
            // ErrorMessage 응답 전송
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"code\":\"INVALID_TOKEN\", \"message\":\"" + e.getMessage() + "\"}");
            return;
        }

        
        // User를 가져와 SecurityContext에 저장한다.
        
        
        try{
        	System.out.println(decodedToken.getUid());
            UserDetails user = userDetailsService.loadUserByUserUid(decodedToken.getUid());
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities());        
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch(NullPointerException | NoSuchElementException e){
            // ErrorMessage 응답 전송
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"code\":\"USER_NOT_FOUND\"}");
            return;
        }
        
        //System.out.println("토큰");
        filterChain.doFilter(request, response);
    }
}