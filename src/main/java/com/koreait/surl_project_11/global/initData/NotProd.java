package com.koreait.surl_project_11.global.initData;

import com.koreait.surl_project_11.domain.article.article.entity.Article;
import com.koreait.surl_project_11.domain.article.article.service.ArticleService;
import com.koreait.surl_project_11.domain.member.member.entity.Member;
import com.koreait.surl_project_11.domain.member.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;

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

    private final ArticleService articleService;
    private final MemberService memberService;

    @Bean //빈을 스프링부트에 등록 : 개발자가 new 하지 않아도 스프링부트가 직접 관리하는 객체 (실행될 때 자동으로 생성해서 관리 시작)
    public ApplicationRunner initNotProd() {
        return args -> {
            //트랜젝션을 두개 만들어보자
            self.work1();
            self.work2();
        };
    }

    //서비스의 모든 public 메서드에서는 @Transactional을 붙이는 게 관례
    //그 중에서 SELECT만 하는 메서드는 @Transactional(readOnly = true)를 붙여야 함
    //클래스 수준에서 @Transactional을 붙인다 ==> 알아서 하위 method에 @Transactional을 붙인다
    //@Transactional이 붙은 메서드 내에서 @Transactional이 붙은 메서드를 실행하면, 물리 트랜젝션은 가장 바깥쪽의 메세드 기준으로 1개만 작동함
    //하위에서 에러가 발생했어도 맨 상위의 메서드에서 롤백이 발생한다는 의미
    //다만 논리 트랜잭션은 @Transactional이 붙은 메서드가 호출 될 때마다 작동함
    //하지만 중요한 것은 물리 트랜잭션
    //논리 트랜젝션 안에서 RuntimeException 계열의 예외가 발생하면 조용한 롤백(==slient rollback)이 일어남
    //==> try-catch로 예외가 발생하는 메서드를 감싸도 소용없음
    //@Transactional(noRollbackFor = {엑셉션 클래스명}.class == 특정 예외에 대해서 조용한 롤백이 발생하지 않도록 할 수 있다

    //@Transactional(noRollbackFor = GlobalException.class) //이렇게 하면 GlobalException이 발생했을 땐 롤백 안한다
    @Transactional //트랜젝션(모든 과정이 성공해야 커밋되도록 안쪽의 과정을 하나의 단위로 묶음)으로 만듦
    public void work1() {
        System.out.println("Not Prod.initNotProd 실행");

        //참고 : 테이블 초기화 ==> truncate 명령어
        //delete는 다 지우고 다시 실행하면 auto_increment가 기존 데이터 + 1에서 시작하지만
        //truncate은 초기화하는 것이기 때문에 다시 1부터 시작한다.

        //rows를 세어줌 == select count(*) from article;
        if (articleService.count() > 0) return; //방법 1: 0개보다 많이 있으면 아래는 실행 안함

        //방법 2 : deleteAll하고 실행
        //articleRepository.deleteAll();

        Member member1 = memberService.join("user1", "1234", "유저 1").getData();
        Member member2 = memberService.join("user2", "1234", "유저 2").getData();

        //중복 가입 에러 발생 시켜 보기
        //try-catch로 감쌌기 때문에 그냥 넘어갈 것 같지만, join도 transaction이고 work1도 transaction이기 때문에
        //어쨌든 부분 실패 == 완전 실패로서 rollback 하게 된다
//        try {
//            RsData<Member> joinRs = memberService.join("user2", "1234", "유저 2");
//        } catch (GlobalException e) {
//            System.out.println("e.getMsg() : " + e.getRsData().getMsg());
//            System.out.println("e.getStatusCode() : " + e.getRsData().getStatusCode());
//        }

        //sevice 도입 전 여기서 빌드 했었던 흔적...
        //빌드할 때 id를 정해주지 않았으므로 0, 0으로 나옴 : DB에 넣기 전에 ID가 정해진다? NO! 애초에 그래서는 안됨
//        System.out.println("DB에 넣기 전 id 1 : " + article1.getId());
//        System.out.println("DB에 넣기 전 id 2 : " + article2.getId());

        Article article1 = articleService.write(member1, "제목 1", "내용 1").getData();
        Article article2 = articleService.write(member1, "제목 2", "내용 2").getData();

        Article article3 = articleService.write(member2, "제목 3", "내용 3").getData();
        Article article4 = articleService.write(member2, "제목 4", "내용 4").getData();

        //DB에 save를 한 후에 id가 생성된다.
//        System.out.println("DB에 넣은 후 id 1 : " + article1.getId());
//        System.out.println("DB에 넣은 후 id 2 : " + article2.getId());

        //**트랜젝션일 때는**, 아래처럼 객체만 변경되어도 변경된 데이터로 DB에 최종 삽입된다. 이유는 ==>
        //**트랜젝션 커밋을 보내기 전에**, 영속성 컨텍스트 상태(DB에 넣을 데이터라고 이해)와 현재의 객체 상태를 비교 확인하여서,
        //'더티(==DB의 내용과 객체의 내용이 달라지는 상황)하지 않게 하기 위해' 알아서 다시 업데이트해 DB에 넣기 때문.
        article2.setTitle("제목 2-2");

        articleService.delete(article1);
    }

    @Transactional
    public void work2() {

    }
}


//아래는 필기

//레이어드 아키텍쳐
//컨트롤러 -> 서비스 -> 리포지터리  -> (JDBC) -> DB


//Optional은 List와 유사. 차이 : List에 넣을 수 있는 개수는 0 ~ N / Optional에 넣을 수 있는 개수는 0 ~ 1 (여러개 가능/한개가 최대)

//조건 있는 select
//0개 혹은 1개므로 Optional에 담아도 됨 (JAP에서 제공해주는 이 함수 findBy조건()이 애초에 Optional을 리턴하게 되어있다.)
//        Optional<Article> opArticle = articleService.findById(2L);

//Article article = opArticle.get();

//조건 없이 select
//N개일 수 있으므로 List에 담아야함 (JAP에서 제공해주는 이 함수 findAll()이 애초에 List를 리턴하게 되어있다.)
//상속 경로에 따라 findAll()의 리턴타입은 바뀔 수도 있음에 주의
//        List<Article> articles = articleService.findAll();
//
//        List<Article> articlesInId = articleService.findByIdInOrderByTitleDescIdAsc(List.of(1L, 2L, 3L));
//        List<Article> articlesByTitle = articleService.findByTitleContaining("제목");
//        List<Article> articlesByTitleAndBody = articleService.findByTitleAndBody("제목", "내용");
