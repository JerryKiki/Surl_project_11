package com.koreait.surl_project_11.domain.article.article.repository;

import com.koreait.surl_project_11.domain.article.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//extends의 제네릭의 두번째가 현재 Long인 이유 : Article의 pk가 long이라서 (Long과 long은 호환됨)
public interface ArticleRepository extends JpaRepository<Article, Long> {
    //메서드명을 기반으로 하는 함수를 만들 수 있다!! 일종의 규칙이 있음. ==> 직접 하는것보다 쿼리를 짠 다음 GPT 시키는게 낫다.
    //단, 적어도 쿼리는 짤 줄 알아야 한다!

    //아래는 학습용으로 만든 함수

    // 1. id가 1, 2, 3인 데이터를 title 기준으로 내림차순 정렬하고, id 기준으로 오름차순 정렬하여 가져오는 함수
    List<Article> findByIdInOrderByTitleDescIdAsc(List<Long> ids);

    // 2. title에 '안녕'이 포함된 데이터를 가져오는 함수
    List<Article> findByTitleContaining(String title);

    // 3. title이 '안녕'이고 body가 '잘가'인 데이터를 가져오는 함수
    List<Article> findByTitleAndBody(String title, String body);

}

/*
목표

1.
SELECT *
FROM article
WHERE id IN (1, 2, 3)
ORDER BY title DESC, id ASC;

2.
SELECT *
FROM article
WHERE title LIKE '%안녕%';

3.
SELECT *
FROM article
WHERE title = '안녕'
AND body = '잘가';
 */