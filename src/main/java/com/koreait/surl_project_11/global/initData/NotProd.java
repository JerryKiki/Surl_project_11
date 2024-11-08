package com.koreait.surl_project_11.global.initData;

import com.koreait.surl_project_11.domain.article.article.entity.Article;
import com.koreait.surl_project_11.domain.article.article.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.Optional;

@Profile("!prod") //not production == dev or test 환경에 실행하겠다. (이 클래스의 경우는 테스트 데이터 생성용)
@Configuration
@RequiredArgsConstructor
public class NotProd {

    @Lazy
    @Autowired
    //this를 통한 객체 내부에서의 메서드 호출은 @Transactional을 작동시키지 않음
    //외부객체에 의한 메서드 호출은 @Transactional이 작동한다.
    //그래서 이렇게 self를 객체로 만들어서 사용함
    //@Lazy, @Autowirde 조합으로 this의 외부 호출 모드 버전 self를 얻을 수 있다.
    //self를 통한 메서드 호출은 @Transactional을 작동시킬 수 있다.
    private NotProd self;

    private final ArticleRepository articleRepository;

    @Bean //빈을 스프링부트에 등록 : 개발자가 new 하지 않아도 스프링부트가 직접 관리하는 객체 (실행될 때 자동으로 생성해서 관리 시작)
    public ApplicationRunner initNotProd() {
        return args -> {
            //트랜젝션을 두개 만들어보자
            self.work1();
            self.work2();
        };
    }

    @Transactional //트랜젝션(모든 과정이 성공해야 커밋되도록 안쪽의 과정을 하나의 단위로 묶음)으로 만듦
    public void work1() {
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

        //이렇게 바꿨을 때, '더티(==DB의 내용과 객체의 내용이 달라지는 상황)하지 않게 하기 위해' 알아서 다시 db에 업데이트한다.
        article2.setTitle("제목 2-2");

        articleRepository.delete(article1);
    }

    @Transactional
    public void work2() {

        //Optional은 List와 유사. 차이 : List에 넣을 수 있는 개수는 0 ~ N / Optional에 넣을 수 있는 개수는 0 ~ 1 (여러개 가능/한개가 최대)

        //조건 있는 select
        //0개 혹은 1개므로 Optional에 담아도 됨 (JAP에서 제공해주는 이 함수 findBy조건()이 애초에 Optional을 리턴하게 되어있다.)
        Optional<Article> opArticle = articleRepository.findById(2L);

        //Article article = opArticle.get();

        //조건 없이 select
        //N개일 수 있으므로 List에 담아야함 (JAP에서 제공해주는 이 함수 findAll()이 애초에 List를 리턴하게 되어있다.)
        //상속 경로에 따라 findAll()의 리턴타입은 바뀔 수도 있음에 주의
        List<Article> articles = articleRepository.findAll();

        List<Article> articlesInId = articleRepository.findByIdInOrderByTitleDescIdAsc(List.of(1L, 2L, 3L));
        List<Article> articlesByTitle = articleRepository.findByTitleContaining("제목");
        List<Article> articlesByTitleAndBody = articleRepository.findByTitleAndBody("제목", "내용");
    }

}
