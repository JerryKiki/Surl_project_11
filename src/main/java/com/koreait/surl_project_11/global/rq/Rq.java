package com.koreait.surl_project_11.global.rq;

import com.koreait.surl_project_11.domain.member.member.entity.Member;
import com.koreait.surl_project_11.domain.member.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope //Rq 클래스의 생명주기를 바꾼다. ==> 요청이 들어올 때마다 Rq 객체가 하나씩 생기고, 요청이 끝나면 죽는다.
@RequiredArgsConstructor
public class Rq {

    private final HttpServletRequest req;
    private final HttpServletResponse resp;
    private final MemberService memberService;

    public Member getMember() {
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
        return memberService.getReferenceById(3L); //user1
    }

    public String getCurrentUrlPath() {
        return req.getRequestURI();
    }

    public void setStatusCode(int statusCode) {
        resp.setStatus(statusCode);
    }

}
