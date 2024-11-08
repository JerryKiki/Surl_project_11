package com.koreait.surl_project_11.domain.article.article.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity //이 클래스로 테이블 만들거야
@Builder
@Getter
@NoArgsConstructor //jpa의 어떤 함수들이 이용하도록 하기 위해 필요
@AllArgsConstructor //builder를 위해 필요
public class Article {

    @Id //이 필드를 PRIMARY KEY로 만들거야
    @GeneratedValue(strategy = IDENTITY) //프라이머리키 값을 자동으로 생성할 때 사용 (==AUTO_INCREMENT)
    private long id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String body;
}
