package com.koreait.surl_project_11.domain.member.member.controller;

import com.koreait.surl_project_11.domain.member.member.entity.Member;
import com.koreait.surl_project_11.domain.member.member.service.MemberService;
import com.koreait.surl_project_11.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //자동으로 Responsebody를 붙여줌
@RequestMapping("/api/v1/members") //이 요청에는 이 controller가 반응 : 여기서 v1 뒤에 저기는 복수형인 것이 관례
@RequiredArgsConstructor
@Slf4j
public class ApiV1MemberController {
    private final MemberService memberService;

    //입력받고 싶은거
    @AllArgsConstructor
    @Getter
    public static class MemberJoinReqBody {
        //private이지만 내부 클래스라서 MemberController Class 내에서는 getter 없이 사용 가능함
        @NotBlank(message = "username을 입력하세요")
        private String username;
        @NotBlank(message = "password를 입력하세요")
        private String password;
        @NotBlank(message = "nickname을 입력하세요")
        private String nickname;
    }

    //응답에 대해서도 짝을 맞춰주자!
    //응답 양식을 맞춰주기 위함
    //내보내고 싶은거
    @AllArgsConstructor
    @Getter
    public static class MemberJoinRespBody {
        Member item;
    }

    //CRUD
    // POST /api/v1/members(/join) => post가 애초에 생성이라는 의미라서 /join은 붙여도 되고 없어도 됨
    @PostMapping("")
    //@ResponseBody 직접 응답할 게 아니라서 딱히 필요는 없음
    public RsData join(@RequestBody @Valid MemberJoinReqBody requestBody) { //Body를 객체화해서 받자... 안쪽에서 내용 정리를 한다

        //아래는 @NotBlank 어노테이션 + @Valid 어노테이션으로 처리할 수 있다... vaildation 의존성 추가 필요!!
        //위치는 위를 참고. (필드 선언부에 @NotBlank, 객체 선언부에 @Valid)

//        if(Ut.str.isBlank(requestBody.username)) {
//            //return RsData.of("400-1", "username is blank");
//            //아래가 더 좋음 : 사유 => 리턴타입을 신경쓸 필요가 없음
//            //대신 유저가 당황할 수 있기 때문에 exception handler를 통해 exception에 따른 처리방식을 구현해두자!
//            throw new GlobalException("400-1", "username is blank");
//        }
//        if(Ut.str.isBlank(requestBody.password)) {
//            throw new GlobalException("400-2", "password is blank");
//        }
//        if(Ut.str.isBlank(requestBody.nickname)) {
//            throw new GlobalException("400-3", "nickname is blank");
//        }

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

        //리턴의 타입과 형태 맞춰주기
        RsData<Member> joinRs = memberService.join(requestBody.username, requestBody.password, requestBody.nickname);
        return joinRs.newDataOf(new MemberJoinRespBody(joinRs.getData()));
    }

//    @GetMapping("/testThrowIllegalArgumentException")
//    //@ResponseBody
//    public RsData<Member> testThrowIllegalArgumentException() {
//        throw new IllegalArgumentException("IllegalArgumentException");
//    }
}

/*
- 클라이언트가 접근할 수 있는 하나하나의 메서드를 엔드포인트 혹은 액션메서드라고 한다.
- 액션메서드 위에 관련된 클래스를 두는 것이 추천된다.
- 액션 메서드마다 보통 클래스가 2개씩 필요하다.
  - 요청 본문 클래스 : 입력값이 없다면 필요없음
  - 응답 본문 클래스 : 예시로 들은 것 == MemberJoinRespBody 클래스 => 목적 : join 메서드가 RsData<MemberJoinRespBody> 타입 데이터를 리턴하도록 함
 */