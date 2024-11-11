package com.koreait.surl_project_11.global.initData;

import com.koreait.surl_project_11.domain.article.article.service.ArticleService;
import com.koreait.surl_project_11.domain.member.member.entity.Member;
import com.koreait.surl_project_11.domain.member.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class All { //언제나 생성됨

    @Lazy
    @Autowired
    private All self;

    private final ArticleService articleService;
    private final MemberService memberService;

    @Bean
    @Order(3) //서버 시작 시 실행 순서 : 다른 모든것들이 실행된 다음 실행되어야하므로 3번으로 시작됨 (멤버부터 제작)
    public ApplicationRunner initAll() {
        return args -> {
            self.work1();
        };
    }

    @Transactional
    public void work1() {
        log.debug("initAll started");

        if(memberService.count() > 0) return; //멤버가 이미 있으면 추가 X

        //system, admin 두개는 보통 관례
        Member memberSystem = memberService.join("system", "1234", "시스템").getData();
        Member memberAdmin = memberService.join("admin", "1234", "관리자").getData();
        Member member1 = memberService.join("user1", "1234", "회원1").getData();
        Member member2 = memberService.join("user2", "1234", "회원2").getData();

    }
}
