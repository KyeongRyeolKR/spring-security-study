package com.example.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 인가 설정
                .authorizeHttpRequests(
                        (auth) -> auth
                                .requestMatchers("/", "/login").permitAll() // /, /login 경로는 모두에게 허용
                                .requestMatchers("/admin").hasRole("ADMIN") // /admin 경로는 ADMIN만 허용
                                .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER") // /my/** 경로는 ADMIN, USER만 허용
                                .anyRequest().authenticated() // 나머지 요청은 로그인한 사용자만 허용
                );

        return http.build();
    }
}
