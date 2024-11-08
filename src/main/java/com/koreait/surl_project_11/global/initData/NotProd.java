package com.koreait.surl_project_11.global.initData;

import com.koreait.surl_project_11.domain.article.article.entity.Article;
import com.koreait.surl_project_11.domain.article.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!prod") //not production == dev or test 환경에 실행하겠다. (이 클래스의 경우는 테스트 데이터 생성용)
@Configuration
@RequiredArgsConstructor
public class NotProd {

    private final ArticleRepository articleRepository;

    @Bean //빈을 스프링부트에 등록 : 개발자가 new 하지 않아도 스프링부트가 직접 관리하는 객체 (실행될 때 자동으로 생성해서 관리 시작)
    public ApplicationRunner initNotProd() {
        return args -> {
            System.out.println("Not Prod.initNotProd 실행");

            //rows를 세어줌 == select count(*) from article;
            if (articleRepository.count() > 0) return; //0개보다 많이 있으면 아래는 실행 안함

            //방법 2 : deleteAll하고 실행
            //articleRepository.deleteAll();

            //참고 : 테이블 초기화 ==> truncate 명령어
            //delete는 다 지우고 다시 실행하면 auto_increment가 기존 데이터 + 1에서 시작하지만
            //truncate은 초기화하는 것이기 때문에 다시 1부터 시작한다.

            Article article1 = Article.builder()
                            .title("제목1")
                            .body("내용1")
                            .build();

            Article article2 = Article.builder()
                    .title("제목2")
                    .body("내용2")
                    .build();

            //빌드할 때 id를 정해주지 않았으므로 0, 0으로 나옴 : DB에 넣기 전에 ID가 정해진다? NO! 애초에 그래서는 안됨
            System.out.println("DB에 넣기 전 id 1 : " + article1.getId());
            System.out.println("DB에 넣기 전 id 2 : " + article2.getId());

            articleRepository.save(article1);
            articleRepository.save(article2);

            //DB에 save를 한 후에 id가 생성된다.
            System.out.println("DB에 넣은 후 id 1 : " + article1.getId());
            System.out.println("DB에 넣은 후 id 2 : " + article2.getId());
        };
    }
}
