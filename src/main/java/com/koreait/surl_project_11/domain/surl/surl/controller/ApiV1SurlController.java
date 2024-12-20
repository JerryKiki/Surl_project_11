package com.koreait.surl_project_11.domain.surl.surl.controller;

import com.koreait.surl_project_11.domain.auth.auth.service.AuthService;
import com.koreait.surl_project_11.domain.member.member.entity.Member;
import com.koreait.surl_project_11.domain.member.member.service.MemberService;
import com.koreait.surl_project_11.domain.surl.surl.dto.SurlDto;
import com.koreait.surl_project_11.domain.surl.surl.entity.Surl;
import com.koreait.surl_project_11.domain.surl.surl.service.SurlService;
import com.koreait.surl_project_11.global.exceptions.GlobalException;
import com.koreait.surl_project_11.global.rq.Rq;
import com.koreait.surl_project_11.global.rsData.RsData;
import com.koreait.surl_project_11.standard.dto.Empty;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/surls")
//@RequestMapping(value = "/api/v1/surls", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ApiV1SurlController {

    private final Rq rq;
    private final SurlService surlService;
    private final AuthService authService;
    private final MemberService memberService;

    @PostMapping("")
    @ResponseBody
    @Transactional
    @Operation(summary = "생성")
    public RsData<SurlAddRespBody> add(
            @RequestBody @Valid SurlAddReqBody reqBody
    ) {
        Member member = rq.getMember(); // 현재 브라우저로 로그인 한 회원 정보

        RsData<Surl> addRs = surlService.add(member, reqBody.body, reqBody.url);

        return addRs.newDataOf(
                new SurlAddRespBody(
                        new SurlDto(addRs.getData())
                )
        );
    }

    // /api/v1/surls/{id}
    // /api/v1/surls/1
    // /api/v1/surls?id=1
    @GetMapping("/{id}")
    @Operation(summary = "단건조회")
    public RsData<SurlGetRespBody> get(
            @PathVariable long id,
            String actorUsername
    ) {
        Surl surl = surlService.findById(id).orElseThrow(GlobalException.E404::new);

//        Member member = rq.getMember();

//        //소유권 체크 : 조회, 수정, 삭제 전에 행위자(로그인 한 사람)가 surl 객체의 소유주인지 체크
//        //rq.getMember() ==> 현재는 id가 3번인 회원(User1)이 리턴되는 중 ==> 개선 완
//        if(!surl.getAuthor().equals(member)) { //id가 'L'ong이라서 equals로 객체비교를 해주는 게 좋다.
//            throw new GlobalException("403-1", "권한이 없습니다.");
//        }

        authService.checkCanGetSurl(rq.getMember(), surl);

        return RsData.of(
                new SurlGetRespBody(new SurlDto(surl))
        );
    }

    @GetMapping("")
    @Operation(summary = "다건조회")
    public RsData<SurlGetItemsRespBody> getItems() {

//        Member loginedMember = memberService.findByUserName(actorUsername).orElseThrow(GlobalException.E404::new);
//        rq.setMember(loginedMember);

        //rq에서 req를 통해 setting하게 한 뒤로는 위의 코드가 필요없이 이것만 하면 된다
        Member member = rq.getMember();

        //내가 만든 Surl만 보일 수 있도록.
        List<Surl> surls = surlService.findByAuthorOrderByIdDesc(member);

        // Page
        // QueryDSL
        // 위에거 찾아보자

        return RsData.of(
                new SurlGetItemsRespBody(
                        surls.stream()
                                .map(SurlDto::new)
                                .toList()
                )
        );
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "삭제")
    public RsData<Empty> delete(
            @PathVariable long id
    ) {
        Surl surl = surlService.findById(id).orElseThrow(GlobalException.E404::new);

        authService.checkCanDeleteSurl(rq.getMember(), surl);

        surlService.delete(surl);

        return RsData.OK;
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "수정")
    public RsData<SurlModifyRespBody> modify(
            @PathVariable long id,
            @RequestBody @Valid SurlModifyReqBody reqBody
    ) {
        Surl surl = surlService.findById(id).orElseThrow(GlobalException.E404::new);

        authService.checkCanModifySurl(rq.getMember(), surl);

        RsData<Surl> modifyRs = surlService.modify(surl, reqBody.body, reqBody.url);

        return modifyRs.newDataOf(
                new SurlModifyRespBody(
                        new SurlDto(modifyRs.getData())
                )
        );
    }

    @AllArgsConstructor
    @Getter
    public static class SurlAddReqBody {
        @NotBlank
        private String body;
        @NotBlank
        private String url;
    }

    @AllArgsConstructor
    @Getter
    public static class SurlAddRespBody {
        private SurlDto item;
    }

    @AllArgsConstructor
    @Getter
    public static class SurlGetRespBody {
        private SurlDto item;
    }

    @AllArgsConstructor
    @Getter
    public static class SurlGetItemsRespBody {
        private List<SurlDto> items;
    }

    @AllArgsConstructor
    @Getter
    public static class SurlModifyReqBody {
        @NotBlank
        private String body;
        @NotBlank
        private String url;
    }

    @AllArgsConstructor
    @Getter
    public static class SurlModifyRespBody {
        private SurlDto item;
    }

}