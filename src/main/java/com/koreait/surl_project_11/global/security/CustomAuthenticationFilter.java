package com.koreait.surl_project_11.global.security;

import com.koreait.surl_project_11.domain.auth.auth.service.AuthTokenService;
import com.koreait.surl_project_11.domain.member.member.entity.Member;
import com.koreait.surl_project_11.domain.member.member.service.MemberService;
import com.koreait.surl_project_11.global.app.AppConfig;
import com.koreait.surl_project_11.global.rq.Rq;
import com.koreait.surl_project_11.standard.util.Ut;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;
import java.util.Map;

//쿠키가 있으면 인증이 된 상태야! 라는 걸 시큐리티에게 알려주기 위한 필터클래스.
//필터기능 구현
//인증정보가 없거나 올바르지 않으면 예외 발생 X => 시큐리티 인증정보를 등록하지 않는 걸로
//Why? : 어차피, 인증정보를 등록하지 않는 것만으로도 사용자가 원하는 목적지로 갈 수 없기 때문에.
//대신 쿠키의 인증정보가 올바르다면 시큐리티에 인증정보를 등록
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    private final MemberService memberService;
    private final Rq rq;
    private final AuthTokenService authTokenService;

    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain filterChain) {
//        String actorUsername = rq.getCookieValue("actorUsername", null);
//        String actorPassword = rq.getCookieValue("actorPassword", null);
        //apiKey 사용으로 변경
//        String apiKey = rq.getCookieValue("apiKey", null);

        //토큰 사용으로 변경
        String accessToken = rq.getCookieValue("accessToken", null);
        //리프레시토큰 추가
        String refreshToken = rq.getCookieValue("refreshToken", null);

        if (accessToken  == null) {
            String authorization = req.getHeader("Authorization");
            if (authorization != null) {
//                accessToken = authorization.substring("bearer ".length());
                String[] authorizationBits = authorization.substring("bearer ".length()).split(" ", 2);
                if (authorizationBits.length == 2) {
                    accessToken = authorizationBits[0];
                    refreshToken = authorizationBits[1];
                }
            }
        }
        if (Ut.str.isBlank(accessToken) || Ut.str.isBlank(refreshToken)) {
            filterChain.doFilter(req, resp);
            return;
        }

//        Member loginedMember = memberService.findByApiKey(apiKey).orElse(null);
        if (!authTokenService.validateToken(accessToken)) {
//            filterChain.doFilter(req, resp);
//            return;
            Member member = memberService.findByRefreshToken(refreshToken).orElse(null);
            if (member == null) {
                filterChain.doFilter(req, resp);
                return;
            }
            String newAccessToken = authTokenService.genToken(member, AppConfig.getAccessTokenExpirationSec());
            rq.setCookie("accessToken", newAccessToken);
            log.debug("accessToken renewed: {}", newAccessToken);
            accessToken = newAccessToken;
        }

        //Member의 정보를 담은 User(==Security가 이해하는 Member의 형태)를 알려준다.
//        User user = new User(loginedMember.getUsername(), "", List.of());
        //id 사용으로 변경
//        User user = new User(loginedMember.getId() + "", "", List.of());
        //토큰 사용을 위한 변경
        Map<String, Object> accessTokenData = authTokenService.getDataFrom(accessToken);
        long id = (int) accessTokenData.get("id");
        User user = new User(id + "", "", List.of());
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(req, resp); //필터를 종료하고 다음 턴으로 넘긴다.
    }
}