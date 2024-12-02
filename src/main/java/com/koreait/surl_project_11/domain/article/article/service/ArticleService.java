package com.koreait.surl_project_11.domain.article.article.service;

import com.koreait.surl_project_11.domain.article.article.entity.Article;
import com.koreait.surl_project_11.domain.article.article.repository.ArticleRepository;
import com.koreait.surl_project_11.domain.member.member.entity.Member;
import com.koreait.surl_project_11.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
//select 메소드가 많을 때는, 클래스에 붙이면 readOnly가 붙으면 안되는 놈에게는 자동으로 readOnly=true가 붙어서 안써줘도 되게 됨
//public이 아닌 놈한테는 안붙임
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;

    //@Transactional(readOnly = true)
    public long count() {
        return articleRepository.count();
    }

    //리턴
    // - 이번에 생성된 게시글의 번호
    // - 게시글 생성에 대한 결과 메세지
    // - 결과 코드
    // - 비정상적인 접근(도배) 등 감지되었을 시 실패로 만들기
    @Transactional //readOnly = true면 안되는 경우에는 명시해줌
    public RsData<Article> write(Member member, String title, String body) {
        Article article = Article
                .builder()
                .author(member)
                .title(title)
                .body(body)
                .build();

        articleRepository.save(article);
        return RsData.of("%d번 게시글이 생성됨".formatted(article.getId()), article);
    }

    @Transactional //readOnly = true면 안되는 경우에는 명시해줌
    public void delete(Article article1) {
        articleRepository.delete(article1);
    }

    //@Transactional(readOnly = true)
    public Optional<Article> findById(long id) {
        return articleRepository.findById(id);
    }

    // @Transactional(readOnly = true)
    public List<Article> findAll() {
        return articleRepository.findAll();
    }
}
