package com.koreait.surl_project_11.global.jpa.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static jakarta.persistence.GenerationType.IDENTITY;

//이런 클래스 사용하는 이유 : 중복 코드 제거!
//객체 입장에서 공통 매핑 정보가 필요할 때 부모를 만들고... 부모한테 붙여준다. (얘는 어떤 클래스의 부모로 쓸거야라고 알려줌)
@MappedSuperclass
@Getter
//ex: member1.equals(member2) 같은 거 하고 싶을 때... 얘는 객체 전체 비교를 하지 않고 member1의 id와 member2의 id만을 비교함
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    public String getModelName() {
        String simpleName = this.getClass().getSimpleName();
        return Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
    }
}
