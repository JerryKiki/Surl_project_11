package com.koreait.surl_project_11.domain.member.member.controller;

import com.koreait.surl_project_11.domain.member.member.entity.Member;
import com.koreait.surl_project_11.domain.member.member.service.MemberService;
import com.koreait.surl_project_11.global.exceptions.GlobalException;
import com.koreait.surl_project_11.global.rsData.RsData;
import com.koreait.surl_project_11.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/join")
    @ResponseBody
    public RsData join(String username, String password, String nickname) {

        if(Ut.str.isBlank(username)) {
            //return RsData.of("400-1", "username is blank");
            //아래가 더 좋음 : 사유 => 리턴타입을 신경쓸 필요가 없음
            //대신 유저가 당황할 수 있기 때문에 exception handler를 통해 exception에 따른 처리방식을 구현해두자!
            throw new GlobalException("400-1", "username is blank");
        }
        if(Ut.str.isBlank(password)) {
            throw new GlobalException("400-2", "password is blank");
        }
        if(Ut.str.isBlank(nickname)) {
            throw new GlobalException("400-3", "nickname is blank");
        }

        //서비스에서 예외가 발생하면 catch해서 적절한 처리를 해서 '꾸며주기'
//        try {
//            return memberService.join(username, password, nickname);
//        } catch (GlobalException e) {
//            if(e.getRsData().getResultCode().equals("400-1")) {
//                log.debug("Username already exists");
//            }
//            return RsData.of("400-C", "custom exception msg", Member.builder().build());
//        }
        //이것을 ExceptionHandler에서 처리하게 하자!

        return memberService.join(username, password, nickname);
    }

    @GetMapping("/testThrowIllegalArgumentException")
    @ResponseBody
    public RsData<Member> testThrowIllegalArgumentException() {
        throw new IllegalArgumentException("IllegalArgumentException");
    }
}
