package com.koreait.surl_project_11.global.security;

import com.koreait.surl_project_11.domain.member.member.entity.Member;
import com.koreait.surl_project_11.domain.member.member.service.MemberService;
import com.koreait.surl_project_11.global.rq.Rq;
import com.koreait.surl_project_11.standard.util.Ut;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;

//쿠키가 있으면 인증이 된 상태야! 라는 걸 시큐리티에게 알려주기 위한 필터클래스.
//필터기능 구현
//인증정보가 없거나 올바르지 않으면 예외 발생 X => 시큐리티 인증정보를 등록하지 않는 걸로
//Why? : 어차피, 인증정보를 등록하지 않는 것만으로도 사용자가 원하는 목적지로 갈 수 없기 때문에.
//대신 쿠키의 인증정보가 올바르다면 시큐리티에 인증정보를 등록
@Component
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    private final MemberService memberService;
    private final Rq rq;

    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain filterChain) {
//        String actorUsername = rq.getCookieValue("actorUsername", null);
//        String actorPassword = rq.getCookieValue("actorPassword", null);
        //apiKey 사용으로 변경
        String apiKey = rq.getCookieValue("apiKey", null);
        if (apiKey == null) {
            String authorization = req.getHeader("Authorization");
            if (authorization != null) {
//                authorization = authorization.substring("bearer ".length());
//                String[] authorizationBits = authorization.split(" ", 2);
//                actorUsername = authorizationBits[0];
//                actorPassword = authorizationBits.length == 2 ? authorizationBits[1] : null;
                apiKey = authorization.substring("bearer ".length());
            }
        }
        if (Ut.str.isBlank(apiKey)) {
            filterChain.doFilter(req, resp);
            return;
        }
        Member loginedMember = memberService.findByApiKey(apiKey).orElse(null);
        if (loginedMember == null) {
            filterChain.doFilter(req, resp);
            return;
        }

        //Member의 정보를 담은 User(==Security가 이해하는 Member의 형태)를 알려준다.
//        User user = new User(loginedMember.getUsername(), "", List.of());
        //id 사용으로 변경
        User user = new User(loginedMember.getId() + "", "", List.of());
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(req, resp); //필터를 종료하고 다음 턴으로 넘긴다.
    }
}