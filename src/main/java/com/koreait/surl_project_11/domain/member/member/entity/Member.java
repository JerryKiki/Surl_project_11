package com.koreait.surl_project_11.domain.member.member.entity;

import com.koreait.surl_project_11.global.jpa.entity.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Entity //이 클래스로 테이블 만들거야
@Builder
@Getter
@Setter
@NoArgsConstructor(access = PROTECTED) //jpa의 어떤 함수들이 이용하도록 하기 위해 필요
@AllArgsConstructor(access = PROTECTED) //builder를 위해 필요
//@EntityListeners(AuditingEntityListener.class) //CreatedDate와 ModifiedDate 어노테이션이 작동하도록 하는 어노테이션
public class Member extends BaseTime {

//    @Id //이 필드를 PRIMARY KEY로 만들거야
//    @GeneratedValue(strategy = IDENTITY) //프라이머리키 값을 자동으로 생성할 때 사용 (==AUTO_INCREMENT)
//    private long id;
//    @CreatedDate
//    private LocalDateTime createdDate;
//    @LastModifiedDate
//    private LocalDateTime modifyDate;
    @Column(unique = true)
    private String username;
    private String password;
    private String nickname;

    public String getName() {
        return nickname;
    }
}
