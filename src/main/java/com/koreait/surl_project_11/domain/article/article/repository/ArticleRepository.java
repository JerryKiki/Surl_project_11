package com.koreait.surl_project_11.domain.article.article.repository;

import com.koreait.surl_project_11.domain.article.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

//extends의 제네릭의 두번째가 현재 Long인 이유 : Article의 pk가 long이라서 (Long과 long은 호환됨)
public interface ArticleRepository extends JpaRepository<Article, Long> {

}
