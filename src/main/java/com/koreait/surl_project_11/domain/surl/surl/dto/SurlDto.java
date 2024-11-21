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
