package com.koreait.surl_project_11.domain.member.member.service;

import com.koreait.surl_project_11.domain.member.member.entity.Member;
import com.koreait.surl_project_11.domain.member.member.repository.MemberRepository;
import com.koreait.surl_project_11.global.exceptions.GlobalException;
import com.koreait.surl_project_11.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional //service의 public 메소드는 transactional을 붙이는 것이 관례
    //중간에 어떤 상황이든 예외가 났다면 애매한 상태로 남겨두지 않고 롤백을 시켜주는 것이 깔끔
    public RsData<Member> join(String username, String password, String nickname) {

        //중복체크(표현 1과 2는 동일한 기능인데 표현이 다른 것)
        //실패한 RsData를 return해줘도 되지만... Exception을 발생시키는 방법으로 관리하는 것이 여러가지 상황에 대응하기 좋다.
        //표현 1.
//        boolean present = findByUserName(username).isPresent();
//        if(present) {
//            //Runtime Exception 계열 : 예외가 발생해도 실행이 멈추지 않는 것
//            //아래처럼 이미 있는 애인 IllegalArgumentException을 써도 되고...
//            //throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
//            //아래처럼 내가 원하는 Exception을 만들어내도 된다.
//            throw new GlobalException("400-1", "이미 존재하는 아이디입니다.");
//        }

        //표현 2.
        findByUserName(username).ifPresent(ignored -> {
            throw new GlobalException("401-1", "이미 존재하는 아이디야.");
        });

        Member member = Member
                .builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .build();

        memberRepository.save(member);
        return RsData.of("회원가입이 완료되었습니다.", member);
    }

    @Transactional(readOnly = true) //select만 할 것 같으면 readOnly=true를 붙여주는 것이 성능적으로 최적화가 된다
    public long count() {
        return memberRepository.count();
    }

    @Transactional // private에는 붙여도 소용 X
    public Optional<Member> findByUserName(String username) {
        return memberRepository.findByUsername(username);
    }

    public Member getReferenceById(long id) {
        return memberRepository.getReferenceById(id); //findById랑 크게 다를 것 없으나, 'proxy 객체'를 리턴한다.
        //프록시? : '효율적' ==> 동적으로 자신의 안을 채우는 객체 : 비워두었다가, 자신을 '깔려고'하면(==필요에 의해서만) 안쪽을 채운다. + 비용이 싸다.
        //jpa식으로 '비용이 싸다' == 이 객체를 만드는데 sql이 바로 작동하지 않는다. (필요에 의해서만 잠깐 프로그램을 멈춰두고 갔다온다.)
        //슈뢰딩거의 상자처럼 결국 사용자는 무슨 일이 벌어지는지 모르게 된다.
    }
}
