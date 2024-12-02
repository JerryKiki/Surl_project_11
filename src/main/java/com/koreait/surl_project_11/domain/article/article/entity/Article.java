package com.koreait.surl_project_11.domain.article.article.entity;

import com.koreait.surl_project_11.domain.member.member.entity.Member;
import com.koreait.surl_project_11.global.jpa.entity.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import static lombok.AccessLevel.PROTECTED;

@Entity //이 클래스로 테이블 만들거야
@Builder
@Getter
@Setter
//accessLevel을 Protected(부모 자식 간)으로
@NoArgsConstructor(access = PROTECTED) //jpa의 어떤 함수들이 이용하도록 하기 위해 필요
@AllArgsConstructor(access = PROTECTED) //builder를 위해 필요
@EntityListeners(AuditingEntityListener.class) //CreatedDate와 ModifiedDate 어노테이션이 작동하도록 하는 어노테이션
//위에건 Base에 생김으로서 없어져도 되게 되었다
//BaseTime이 BaseEntity를 상속받기 때문에 BaseTime만 해줘도 된다 (여기에 BaseEntity 것도 다 있으니깐)
public class Article extends BaseTime {

    //    @Id //이 필드를 PRIMARY KEY로 만들거야
//    @GeneratedValue(strategy = IDENTITY) //프라이머리키 값을 자동으로 생성할 때 사용 (==AUTO_INCREMENT)
//    private long id;
//    @CreatedDate
//    private LocalDateTime createdDate;
//    @LastModifiedDate
//    private LocalDateTime modifyDate;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String body;
    //기존에 쓰던 방식
    //private Long memberId;
    //private String username;
    @ManyToOne
    //Article 입장에서는 다른 entity에서 온 것이라서 ManyToOne을 달아줌 (이때 Many가 Aritlce, One이 Member : 하나의 회원이 여러개의 글을 쓸 수 있다)
    private Member author; //jpa에서는 이렇게 하는게 관례
}
