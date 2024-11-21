package com.koreait.surl_project_11.domain.surl.surl.dto;

import com.koreait.surl_project_11.domain.surl.surl.entity.Surl;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//Dto를 만드는 이유? : Entity가 바로 노출되면 안됨 (ex: member의 password 등...)
//JsonIgnore를 해도 되는데 그건 임시방편에 불과
//Entity를 건드리지 않고 보이는 내용만을 바뀌도록 하기 위해 필요
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SurlDto {
    private long id;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private long authorId;
    private String authorName;
    private String body;
    private String url;
    private long count;

    public SurlDto(Surl surl) {
        this.id = surl.getId();
        this.createDate = surl.getCreateDate();
        this.modifyDate = surl.getModifyDate();
        this.authorId = surl.getAuthor().getId();
        this.authorName = surl.getAuthor().getName();
        this.body = surl.getBody();
        this.url = surl.getUrl();
        this.count = surl.getCount();
    }
}

/*
[정리]
- 엔티티는 직접적으로 노출되면 안됨
  - @JsonIgnore가 있지만 임시방편
  - 엔티티의 칼럼명을 바꾸지 못함
  - 프론트엔드 앱에서 오류가 발생
- DTO가 필요하다
  - 엔티티는 API를 통해서 노출될 때 무조건 DTO로 변경되어야 함
  - DTO는 엔티티를 포함하면 안됨
    - Member author는 DTO를 포함하는 구조 ==> 기본타입인 long authorId, String authorName 등 필요한 요소를 적절한 필드로 변경
  - DTO 객체는 엔티티 객체로부터 만드는 게 일반적이다
  - DTO는 엔티티와 구조가 거의 비슷함
 */

/*
[DBCP 관련 필기]
- DBCP
  - 스프링부트에선 DB와의 통신 회선(DB 커넥션)을 미리 여러개 만들어 둔다.
  - 요청이 몰릴 때를 대비하여 미리 해놓은 것임
- OSIV
  - 기본적으로 켜져있음 (true)
  - 이로 인해 하나의 요청이 시작될 때 DBCP에서 DB 커넥션 하나가 배정된다
  - 응답이 완료되면 그 때 DB 커넥션이 회수된다
  - 이 방식은 비효율적이지만, 코딩이 편하다.
- REST API에서는 OSIV 끄기 (추천)
  - 스프링부트로 타임리프나 JSP로 MPA를 만들려는 게 아니고 REST API를 만드는 게 목표라면 보통 끈다
  - 대신 컨트롤러의 액션메서드마다 '@Transactional'을 붙여야함
 */
