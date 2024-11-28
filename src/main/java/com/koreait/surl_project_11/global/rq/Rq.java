package com.koreait.surl_project_11.global.rq;

import com.koreait.surl_project_11.domain.member.member.entity.Member;
import com.koreait.surl_project_11.domain.member.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Arrays;

@Component
@RequestScope //Rq 클래스의 생명주기를 바꾼다. ==> 요청이 들어올 때마다 Rq 객체가 하나씩 생기고, 요청이 끝나면 죽는다.
@RequiredArgsConstructor
public class Rq {

    private final HttpServletRequest req;
    private final HttpServletResponse resp;
    private final MemberService memberService;

    private Member member;

    // ==v1==
    //    public Member getMember() {
    //        return memberService.getReferenceById(3L); //user1
    //    }
    //이 부분을 개선해야한다 ==> 지금은 무조건 3번 회원(User1)으로 되어있다.
    //사용자가 요청을 보낼 때 '신분증'을 태워보내서, 요청을 보낸 것이 누구인지 구분할 수 있도록 해줘야함.
    //왜냐면? 내가 만든 하나의 서비스에 사용자는 다수가 될거니까.
    // - 서버는 사용자를 구분하지 못함
    //   - 서버에게 '내 정보 줘'라고 하면... '너 누군데?'라고 함
    //   - 서버에게 '내 정보 줘, 나는 user1이야' 하면... 서버는 'user1'에 대한 정보를 줌
    //   - 차이점 : 매 요청마다 본인이 누군지에 대한 정보(여기서는 user1)를 포함시켜서 보내야 됨
    //   - 'user1'과 같은 정보를 '인증정보'라고 함
    // - surl 다건조회할 때 인증정보를 포함시켜보자!
    //  - 해당 액션메서드(엔드포인트)에서 인증정보를 기준으로 현재 로그인 한 사람이 누구인지 파악하도록 만들기.


    // ==v2==
    //    @Getter
    //    @Setter
    //    private Member member;
    // 위처럼 해서 Controller의 매 엔드포인트 매서드마다 파라미터값으로 유저를 찾아 rq에 setMember, getMember를 하도록 함

    //==v3==
    /*
    - rq.getMember(); -> null을 리턴할 수도 있었음
      - null 리턴을 방지하려면 rq.setMember(...); 를 사전에 했었어야 함.
      - 매 액션마다 해당 작업을 해줘야할까? => 더 좋은 방법을 찾아보자.
    - rq.getMember 메서드만으로 인증처리
      - Rq는 requst scope bean이기 때문에, HttpServletRequest 객체에 접근이 가능함
      - 따라서 파라미터로 넘겨진 actorUsername에 접근이 가능하다
     */
    public Member getMember() {
        //만약 같은 요청에서 두번 이상 getMember를 하면 한번 더 세팅하지 않고 빠르게 리턴하도록.
        //일종의 캐시데이터 방식. 메모리 캐싱. ==> 이걸 더 발전 시켜서 매 요청마다 쿠키로 인증하도록 해보자.
        if (member != null) return member;

        //==v1==
        //rq는 rquest scope bean이라서 가능함. 요청(req)때마다 rq가 하나씩 생기니까 req에서 받아올 수 있음
//        String actorUsername = req.getParameter("actorUsername");
//        //비밀번호 추가해보자.
//        String actorPassword = req.getParameter("actorPassword");

        //==v2==
        //파라미터가 아닌 헤더에서 받아오도록 변경. ==> 다시, 파라미터에 없으면 헤더에서 받아오도록 변경.
        //헤더는 키-밸류 형태로 이루어진, 요청의 구성요소 중 하나. 내가 바꿀 수 없는 구성정보도 존재하는 부분.
        //그러나 추가는 얼마든지 가능하다.
//        String actorUsername = req.getParameter("actorUsername");
//        String actorPassword = req.getParameter("actorPassword");


//        if(actorUsername == null) actorUsername = req.getHeader("actorUsername");
//        if(actorPassword == null) actorPassword = req.getHeader("actorPassword");

        //==v3==
        //다시, Autorization으로 변경.
        //postman tip: header에 하나하나 쓸 필요없이, Authorization에서 inherit auth from parent를 걸고 부모에 bearer token 하나만 걸어도 됨
        //이때는 bearer를 쓸 필요 없이 아이디 비번만 써주면 됨 (생략해도됨, bearer token이라고 명시했으니까)
        //이렇게 하는 이유 : 보안을 위해.
//        if(actorUsername == null || actorPassword == null) {
//            String authorization = req.getHeader("Authorization");
//            if (authorization != null) {
//                authorization = authorization.substring("bearer ".length());
//                String[] authorizationBits = authorization.split(" ", 2);
//                actorUsername = authorizationBits[0];
//                actorPassword = authorizationBits.length == 2 ? authorizationBits[1] : null;
//            }
//        }

        //==v4==
        //쿠키를 적용해보자.
        //쿠키 : 편하게 말하면 '변수' (== 저장공간).
        //단, http 클라이언트와 http 서버가 공유하는 저장공간.
        //저장공간은 클라이언트에 잡힌다.
        //postman 설정 : 부모의 인증을 다시 inherit으로하고, cookie세팅. (cookies -> localhost:8070 입력 -> actorPassword, actorUsername을 key, value로 입력)
        //쿠키는 특정 사이트에 귀속되고, 해당 사이트로의 통신(요청)이 발생하면 관련된 쿠키가 "자동"으로 헤더에 압축되어 포함된다.
        String actorUsername = getCookieValue("actorUsername", null);
        String actorPassword = getCookieValue("actorPassword", null);

        //==v3==의 이 내용은 동일하게 들어가야.
//        if(actorUsername == null || actorPassword == null) {
//            String authorization = req.getHeader("Authorization");
//            if (authorization != null) {
//                authorization = authorization.substring("bearer ".length());
//                String[] authorizationBits = authorization.split(" ", 2);
//                actorUsername = authorizationBits[0];
//                actorPassword = authorizationBits.length == 2 ? authorizationBits[1] : null;
//            }
//        }
//
//        if(Ut.str.isBlank(actorUsername)) throw new GlobalException("401-1", "인증정보(아이디)를 입력해주세요.");
//        if(Ut.str.isBlank(actorPassword)) throw new GlobalException("401-2", "인증정보(비밀번호)를 입력해주세요.");
//
//        Member loginedMember = memberService.findByUserName(actorUsername).orElseThrow(()-> new GlobalException("403-3", "해당 회원은 존재하지 않습니다."));
////        if(!loginedMember.getPassword().equals(actorPassword)) throw new GlobalException("403-4", "비밀번호가 틀립니다.");
//        //security를 도입했으니 이렇게 수정해주자. 암호화된 패스워드와 입력된 패스워드의 일치/불일치 여부를 판단해주는 함수를 만듦.
//        //Rq에 비즈니스로직을 만들면 좋지 않아서 이렇게 memberService의 함수로 뺌, 여기서 한다고 못하는 건 아님
//        if(!memberService.matchPassword(actorPassword, loginedMember.getPassword())) throw new GlobalException("403-4", "비밀번호가 틀립니다.");
//        member = loginedMember;
//
//        return loginedMember;

        //==v5==
        //CustomAuthenticationFilter에서 인증정보를 체크하고 있으므로 더블체크 제거
//        String name = SecurityContextHolder.getContext().getAuthentication().getName();
//        member = memberService.findByUserName(name).get();

        //==v6==
        //username 대신 아이디 활용
        long id = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        member = memberService.findById(id).get();

        return member;
    }

    //쿠키에 필요한 함수
    public String getCookieValue(String cookieName, String defaultValue) {
        if (req.getCookies() == null) return defaultValue;
        return Arrays.stream(req.getCookies()) // 쿠키 배열을 스트림으로 변환
                .filter(cookie -> cookie.getName().equals(cookieName))// 쿠키의 이름이 매개변수로 쓰이게
                .findFirst() // 첫 번째 요소
                .map(Cookie::getValue) // 존재하면 쿠키 값으로 매핑
                .orElse(defaultValue); // 없으면 기본 값
    }

    //로그아웃을 하려면... 서버에서 클라이언트에 있는 cookie 공간을 비워줘야한다. 어떻게 할까?
    //쿠키에 접근할 수 있다.
    //단, 쿠키에 대한 편집권은 클라이언트에 있음에 주의하자.
    public void removeCookie(String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        resp.addCookie(cookie);
    }

    public void setCookie(String cookieName, String value) {
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setMaxAge(60 * 60 * 24 * 365);
        cookie.setPath("/");
        resp.addCookie(cookie);
    }

    public String getCurrentUrlPath() {
        return req.getRequestURI();
    }
    public void setStatusCode(int statusCode) {
        resp.setStatus(statusCode);
    }

}


