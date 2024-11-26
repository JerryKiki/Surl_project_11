package com.koreait.surl_project_11.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    //시큐리티(경비같은 느낌으로 이해하면 된다)에게 업무를 알려주는 것.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                authorizationRequests -> authorizationRequests
                        .requestMatchers("/h2-console/**").permitAll() //여기 갈때는 제지 안해도 돼(h2 콘솔 접근)
                        .requestMatchers("/actuator/**").permitAll() //여기 갈때는 제지 안해도 돼(배포 관련)
                        .anyRequest().authenticated() //나머지에 대해서는 인증 필요
        ).formLogin(formLogin -> formLogin.permitAll()); //로그인 화면은 허용해 (이거 안하면 로그인창 접근도 거부돼서 그냥 엑세스 거부됨만 뜸)

        return http.build();
    }
}
