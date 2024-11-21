package com.koreait.surl_project_11.domain.article.article.controller;

import com.koreait.surl_project_11.domain.article.article.entity.Article;
import com.koreait.surl_project_11.domain.article.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/article/list")
    @ResponseBody
    public String showList() {
        StringBuilder sb = new StringBuilder();
        List<Article> articles = articleService.findAll();

        //List를 보내면 json 형식으로 표시됨... but 사람이 이해하기 쉽게 보여줘야한다.
        //아래가 json 버전보다는 낫다.
//        sb.append("<h1>게시글 목록</h1>\n");
//
//        sb.append("<ul>\n");
//        for (Article article : articles) {
//            sb.append("<li>");
//            sb.append(article.getTitle());
//            sb.append("</li>\n");
//        }
//        sb.append("</ul>\n");

        //v2
        sb.append("<h1>게시글 목록</h1>\n");

        sb.append("<ul>\n");
        for (Article article : articles) {
            sb.append("<li>");
            sb.append(article.getId());
            sb.append(" | ");
            sb.append(article.getCreateDate().toString().substring(2, 10));
            sb.append(" | ");
            sb.append(article.getModifyDate().toString().substring(2, 10));
            sb.append(" | ");
            sb.append(article.getTitle());
            sb.append(" | ");
            sb.append(article.getBody());
            sb.append(" | ");
            sb.append(article.getAuthor().getNickname());
            sb.append("</li>\n");
        }
        sb.append("</ul>\n");

        return sb.toString();
    }

    @GetMapping("/article/list2")
    @ResponseBody
    public List<Article> showList2() {
        List<Article> articles = articleService.findAll();
        return articles;
    }
}

        /*
        - 기계는 '정보'만 필요하다
        - 사람은 '정보'와 '꾸밈'이 필요하다
        - 스프링부트 앱에서는 '정보'와 '꾸밈'을 모두 처리할 수 있다
          - JSP, 타임리프
        - 스프링부트 앱에서 '정보'만 처리하는 경우
          - 이때는 따로 프론트엔드 서버를 만들어
          - 이럴 때 쓰이는 게 리액트, 스벨트, 앵귤러, 뷰 등
        - 기계가 원하는 것(정보) : {"age" : 22, "name" : "철수"} ==> 일관된 방식으로 오기만 하면 알아먹는다
        - 사람이 원하는 것(정보 + 꾸밈) : 1명의 사람이 있는데 그 사람의 이름은 철수고 나이는 22살이야

        앱 : 사람을 위해서 만드는 것

        사람
        - 정보 + 꾸밈(정보를 적절히 가공해서 보여주는 것)이 필요
        ㄴ 스프링부트 앱
           - 스프링부트만 만들기(전통적인 방식, MPA) => 스프링부트 혼자 정보 + 꾸밈
           - 스프링부트 + 프론트엔드(최근에 새로 나온 방식, SPA) => 스프링부트가 정보를 담당 + 리액트 or 스벨트 같은 애들이 꾸밈을 담당
             - 전용 프론트엔드 앱이 있을 때, 스프링부트로 REST API 서버를 구현한다 ==> 스프링과 프론트의 소통이 필요하기 때문

         REST API를 만들어보자. ==> 프론트엔드와 소통하기 위한 것!
         => 여기서 만드는 것은 사람이 바로 쓰는 것이 아니게 될것. 프론트엔드와 추후에 소통하기 위함
         => "스프링으로 레스트하게 만들어와" == "외부(==프론트)에서 사용할 수 있는 형식으로 만들어와"

         POSTMAN ==> 왜씀?
         - 기계(프론트엔드 앱)와 통신하는 스프링부트 앱을 만드는 행위를 `REST API 서버 구현` or `API 서버 구현`이라고 함.
         - API 서버를 구현하면서 테스트를 할 때는 브라우저만으로 충분하지 않다
           - 브라우저의 주소표시줄로는 GET 요청만 발생시킬 수 있다.
         - 이럴 때 POSTMAN을 사용한다.
           - 해당 툴로 GET, POST, PUT or PATCH, DELETE HTTP 메서드 요청을 할 수 있으므로.
           - 쿼리스트링과 요청 본문도 요청에 담고 결과도 확인이 가능하므로.

           HTTP 메서드
           - GET : 조회
           - POST : 생성
           - PUT or PATCH : 수정
           - DELETE : 삭제

           삭제의 경우
           - 기존 방식 : GET으로 /article/delete/1 or /article/delete?id=1 (동사 느낌)
           - 선호되는 방식 : delete 요청으로 보내는 것이 좋다 ==> /article/1 or /article?id=1 (명사 느낌)

           수정의 경우
           - 기존 방식 : GET으로 /article/modify?id=1&title=제목수정&body=내용수정
           - 선호되는 방식 : put or patch 요청으로 보내는 것이 좋다
             /article/1
             bodyType = application/json
             body = {
                "title" : "제목수정",
                "body" : "내용수정"
             }

             http 요청을 최대한 활용하는 방법으로 보내는 것이 좋다. ==> 무조건 이걸 쓸수만은 없겠지만 이쪽이 선호된다.

             - URL 은 동사형보다는 명사형으로 쓰는게 좋다.
             - 게시물 CRUD 로 알아보는 올바른 HTTP 요청 방법
             - 게시물 생성
               - 잘못된 방법 : GET /article/write?title=제목&body=내용
               - 올바른 방법 : POST /articles
                 헤더, Content-Type : application/json
                 body : {"title": "제목", "body": "내용"}
             - 게시물 1번 수정
               - 잘못된 방법 : GET /article/modify?id=1&title=제목 new&body=내용 new
               - 올바른 방법 : PUT /articles/1
                 헤더, Content-Type : application/json
                 body : {"title": "제목 new", "body": "내용 new"}
             - 게시물 1번 조회
               - 잘못된 방법 : GET /article/detail?id=1
               - 올바른 방법 : GET /articles/1
             - 게시물들 조회
               - 잘못된 방법 : GET /article/list
               - 올바른 방법 : GET /articles
             - 게시물 1번 삭제
               - 잘못된 방법 : GET /article/delete?id=1
               - 올바른 방법 : DELETE /articles/1
         */
