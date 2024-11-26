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
        http
                .authorizeHttpRequests(authorizationRequests ->
                        authorizationRequests
                                .requestMatchers("/h2-console/**").permitAll() //여기 갈때는 제지 안해도 돼(h2 콘솔 접근)
                                .requestMatchers("/actuator/**").permitAll() //여기 갈때는 제지 안해도 돼(배포 관련)
                                .anyRequest().authenticated() //나머지에 대해서는 인증 필요
                )
                //아래는 우선 h2-console을 사용하기 위함임. ( + Rest API를 만들고 있어서 끈것이기도 함.)
                //h2-console과 security는 서로 친한 관계가 아니라서, csrf(공격의 일종)이 켜져있으면 막혀버린다. 그것을 꺼주는 것
                //타임리프, MPA에서는 csrf 설정을 켜둔채로 사용함.
                //But, 지금처럼 Rest API 방식에서는 끄고 사용한다.
                .headers(
                        headers ->
                                headers.frameOptions(
                                        frameOptions ->
                                                frameOptions.sameOrigin()
                                )
                )
                .csrf(
                        csrf ->
                                csrf.disable()
                )
                .formLogin(formLogin -> formLogin.permitAll()); //로그인 화면은 허용해 (이거 안하면 로그인창 접근도 거부돼서 그냥 엑세스 거부됨만 뜸)

        return http.build();
    }
}
