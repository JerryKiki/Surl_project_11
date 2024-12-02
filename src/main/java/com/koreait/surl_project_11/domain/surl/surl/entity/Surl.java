package com.koreait.surl_project_11.domain.surl.surl.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.koreait.surl_project_11.domain.member.member.entity.Member;
import com.koreait.surl_project_11.global.jpa.entity.BaseTime;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class Surl extends BaseTime {

    @ManyToOne
    @JsonIgnore
    private Member author;

    private String body;
    private String url;

    @Setter(AccessLevel.NONE)
    private long count;

    public void increaseCount() {
        count++;
    }
}
