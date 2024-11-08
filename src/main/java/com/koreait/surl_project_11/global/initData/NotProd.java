package com.koreait.surl_project_11.global.initData;

import com.koreait.surl_project_11.domain.article.article.entity.Article;
import com.koreait.surl_project_11.domain.article.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!prod") //not production == dev or test 환경에 실행하겠다.
@Configuration
@RequiredArgsConstructor
public class NotProd {

    private final ArticleRepository articleRepository;

    @Bean //빈을 스프링부트에 등록 : 개발자가 new 하지 않아도 스프링부트가 직접 관리하는 객체 (실행될 때 자동으로 생성해서 관리 시작)
    public ApplicationRunner initNotProd() {
        return args -> {
            System.out.println("Not Prod.initNotProd1");
            System.out.println("Not Prod.initNotProd2");
            System.out.println("Not Prod.initNotProd3");

            Article articleFirst = Article.builder()
                            .title("제목1")
                            .body("내용1")
                            .build();

            Article articleSecond = Article.builder()
                    .title("제목2")
                    .body("내용2")
                    .build();

            //빌드할 때 id를 정해주지 않았으므로 0, 0으로 나옴 : DB에 넣기 전에 ID가 정해진다? NO! 애초에 그래서는 안됨
            System.out.println("DB에 넣기 전 id 1 : " + articleFirst.getId());
            System.out.println("DB에 넣기 전 id 2 : " + articleSecond.getId());

            articleRepository.save(articleFirst);
            articleRepository.save(articleSecond);

            //DB에 save를 한 후에 id가 생성된다.
            System.out.println("DB에 넣은 후 id 1 : " + articleFirst.getId());
            System.out.println("DB에 넣은 후 id 2 : " + articleSecond.getId());
        };
    }
}
