package com.koreait.surl_project_11.global.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    //로그인을 해도 입구컷이 되는 상황 발생! ==> 내 사이트의 "로그인"이 뭔지 시큐리티는 아직 모른다.
    //"어떤 상황이 인증된 상황인지"를 알려주기 위한 클래스가 필요함. 유저와 관리자간의 규칙을 security는 모르기 때문에.
    //우리는 "쿠키에 사용자 정보가 등록되어 있으며, 그 값이 올바른 상태"가 로그인 상태라고 알려줘야함.
    private final CustomAuthenticationFilter customAuthenticationFilter;

    //시큐리티(경비같은 느낌으로 이해하면 된다)에게 업무를 알려주는 것.
    //스프링 시큐리티는 필터로 구현되어 있다.
    //내가 원하는 필터를 적절한 시점에 실행할 수 있다.
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
                .formLogin(formLogin -> formLogin.permitAll()) //로그인 화면은 허용해 (이거 안하면 로그인창 접근도 거부돼서 그냥 엑세스 거부됨만 뜸)
                //로그인 됐다 안됐다를 판단하기 전에 CustomerAuthenticationFilter 먼저 인식해줘.
                //CustomerAuthenticationFilter는 쿠키(actorUsername, actorPassword)를 체크해서,
                //이게 올바르다면 스프링 시큐리티가 이해할 수 있는 방식으로 "이것이 로그인 상태다"라고 알려줘야함.
                .addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
