package com.example.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
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
                                .requestMatchers("/admin").hasAnyRole("ADMIN") // /admin 경로는 ADMIN만 허용
                                .requestMatchers("/my/**").hasAnyRole("USER") // /my/** 경로는 ADMIN, USER만 허용
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
                // 로그아웃 설정
                .logout(
                        (auth) -> auth
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/")
                );

        // 사이트 위조/변조 방지(CSRF) 설정
        // 개발환경에서는 CSRF 토큰이 없어도 동작이 가능하도록 설정을 끔
        // 해당 설정을 하지 않으면, 기본 값 -> enable
        // Restful API 서버의 경우, 세션이 "STATELESS" 하기 때문에 CSRF 설정을 disable해도 문제가 없다!
//        http.csrf(AbstractHttpConfigurer::disable);


        http
                // 다중 로그인 설정
                .sessionManagement(
                        (auth) -> auth
                                .maximumSessions(1) // 하나의 아이디에 대한 다중 로그인 허용 개수
                                .maxSessionsPreventsLogin(true) // 다중 로그인 개수를 초과했을 경우 처리 방법
                                // true -> 초과 시, 새 로그인 차단
                                // false -> 초과 시, 기존 세션 하나 삭제 후 로그인 성공
                );

        http
                // 세션 고정 공격을 방지하기 위한 보호 설정
                .sessionManagement(
                        (auth) -> auth
                                .sessionFixation().changeSessionId()
                                // sessionFixation().none() -> 로그인 시, 세션 정보 변경 안함
                                // sessionFixation().newSession() -> 로그인 시, 세션 새로 생성
                                // sessionFixation().changeSessionId() -> 로그인 시, 동일한 세션에 대한 id 변경
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

    /*
    권한 계층 설정
    requestMatchers() 설정을 할 때, hasAnyRole()에 어떤 권한을 넣으면 그 권한 이상은 모두 가능하다.
     */
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return hierarchy;
    }
}
