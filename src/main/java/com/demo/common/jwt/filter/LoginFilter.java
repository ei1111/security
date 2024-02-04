package com.demo.common.jwt.filter;


import com.demo.common.jwt.util.JwtUtil;
import com.demo.user.domain.JoinRequestDto;
import com.demo.user.infrastructure.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authToken = null;

        try {
            JoinRequestDto joinRequestDto  = new ObjectMapper().readValue(request.getReader(), JoinRequestDto.class);
            String userId = joinRequestDto.getUserId();
            String password = joinRequestDto.getPassword();
            authToken = new UsernamePasswordAuthenticationToken(userId, password);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //authenticationManager에서 검증진행(디비에서 회원정보를 땡겨와서 UserDetails 서비스에서 유저정보를 받고 검증진행)
        return authenticationManager.authenticate(authToken);
    }

    //authenticationManager에서 검증 성공시 실행
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername();

        /*role 추출*/
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(userId, role);
        response.addHeader("Authorization", "Bearer " + token);
    }

    //authenticationManager에서 검증 실패시 실행
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(401);
    }
}
