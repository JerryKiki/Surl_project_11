package com.koreait.surl_project_11.domain.member.member.controller;

import com.koreait.surl_project_11.domain.member.member.service.MemberService;
import com.koreait.surl_project_11.global.exceptions.GlobalException;
import com.koreait.surl_project_11.global.rsData.RsData;
import com.koreait.surl_project_11.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/join")
    @ResponseBody
    public RsData join(String username, String password, String nickname) {

        if(Ut.str.isBlank(username)) {
//            return RsData.of("400-1", "username is blank"); 아래가 더 굿~
            throw new GlobalException("400-1", "username is blank");
        }
        if(Ut.str.isBlank(password)) {
            throw new GlobalException("400-2", "password is blank");
        }
        if(Ut.str.isBlank(nickname)) {
            throw new GlobalException("400-3", "nickname is blank");
        }

        RsData joinRs = memberService.join(username, password, nickname);
        return joinRs;
    }


}
