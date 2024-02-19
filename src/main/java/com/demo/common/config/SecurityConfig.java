package com.demo.common.config;


import com.demo.common.jwt.filter.JwtFilter;
import com.demo.common.jwt.filter.LoginFilter;
import com.demo.common.jwt.util.JwtUtil;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private static final String[] AUTH_WHITELIST = {
            "/api/v1/**", "/swagger-ui/**", "/api-docs", "/swagger-ui-custom.html",
            "/v3/api-docs/**", "/api-docs/**", "/swagger-ui.html"
    };

 /*   @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
*/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Sha512PasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        //cors
        httpSecurity.cors((cors) -> cors.configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                //프론트단에 포트번호
                corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                //POST,GET
                corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
                corsConfiguration.setAllowCredentials(true);
                corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
                //허용할 시간
                corsConfiguration.setMaxAge(3600L);
                corsConfiguration.setExposedHeaders(Collections.singletonList("Authorization"));
                return corsConfiguration;
            }
        }));

        //csrf disable
        httpSecurity.csrf((csrf) -> csrf.disable());

        //FormLogin, BasicHttp 비활성화
        httpSecurity.formLogin((form) -> form.disable());
        httpSecurity.httpBasic(AbstractHttpConfigurer::disable);

        //경로별 인가 작업
        httpSecurity.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/**").permitAll()
                        //.requestMatchers("/admin").hasRole("ADMIN").anyRequest().authenticated()
                       // .anyRequest().permitAll()
        );

        //제일 중요한 세션 STATELESS 처리
        httpSecurity.sessionManagement(
                sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //커스텀 필터 등록
        httpSecurity.addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class);
        httpSecurity.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}