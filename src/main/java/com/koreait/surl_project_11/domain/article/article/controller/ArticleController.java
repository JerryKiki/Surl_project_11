package com.koreait.surl_project_11.domain.article.article.controller;

import com.koreait.surl_project_11.domain.article.article.entity.Article;
import com.koreait.surl_project_11.domain.article.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
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
         */
