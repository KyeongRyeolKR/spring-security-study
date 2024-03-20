package com.example.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
                                .requestMatchers("/", "/login", "/loginProc", "/join", "/joinProc").permitAll() // /, /login 경로는 모두에게 허용
                                .requestMatchers("/admin").hasRole("ADMIN") // /admin 경로는 ADMIN만 허용
                                .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER") // /my/** 경로는 ADMIN, USER만 허용
                                .anyRequest().authenticated() // 나머지 요청은 로그인한 사용자만 허용
                );

        http
                // 커스텀 로그인 페이지 설정
                .formLogin(
                        (auth) -> auth
                                .loginPage("/login")
                                .loginProcessingUrl("/loginProc")
                                .permitAll()
                );

        http
                // 사이트 위조/변조 방지(CSRF) 설정
                // 개발환경에서는 CSRF 토큰이 없어도 동작이 가능하도록 설정을 끔
                .csrf(
                        AbstractHttpConfigurer::disable
                );

        return http.build();
    }

    /*
    단방향 비밀번호 암호화 라이브러리
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
