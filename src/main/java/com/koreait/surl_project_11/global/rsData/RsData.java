package com.koreait.surl_project_11.global.rsData;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.koreait.surl_project_11.standard.dto.Empty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import static lombok.AccessLevel.PRIVATE;

//Empty는 // Spring Doc + openapi fetch을 위한 클래스. 미리 해둠.

@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PRIVATE)
@Getter
public class RsData<T> {
    public static final RsData<Empty> OK = of("200-1", "성공", new Empty());
    public static final RsData<Empty> FAIL = of("500-1", "실패", new Empty());

    @NonNull
    String resultCode;
    @NonNull
    int statusCode;
    @NonNull
    String msg;
    @NonNull
    T data; //다양한 데이터타입을 사용하기 위함

    public static RsData<Empty> of(String msg) {
        return of("200-1", msg, new Empty());
    }
    public static <T> RsData<T> of(T data) {
        return of("200-1", "성공", data);
    }
    public static <T> RsData<T> of(String msg, T data) {
        return of("201-1", msg, data);
    }
    public static <T> RsData<T> of(String resultCode, String msg) {
        return of(resultCode, msg, (T) new Empty());
    }
    public static <T> RsData<T> of(String resultCode, String msg, T data) {
        int statusCode = Integer.parseInt(resultCode.split("-", 2)[0]);
        RsData<T> tRsData = new RsData<>(resultCode, statusCode, msg, data);
        return tRsData;
    }
    @NonNull
    @JsonIgnore
    public boolean isSuccess() {
        return getStatusCode() >= 200 && getStatusCode() < 400;
    }
    @NonNull
    @JsonIgnore
    public boolean isFail() {
        return !isSuccess();
    }
    public <T> RsData<T> newDataOf(T data) {
        return new RsData<>(resultCode, statusCode, msg, data);
    }
}

/*
## RsData 복습 ==> 사용하는 이유

memberService.join("user1", "1234", "김철수")를 한다고 했을때...
- 실패 1 : user1은 이미 사용중
- 실패 2 : 비번과 비번 확인이 틀려 / 비번에 필요한 요소가 모두 포함되지 않았어
- 성공 1 : 완전 성공
- 성공 2 : 부분 성공 (ex: 계정을 만드는 건 성공했는데 너 이거 인증 더 해야돼)

위를 표현하기 위해서...

public class MemberServie {
    boolean join() {

    }
}

...이걸 쓴다? ==> true, false밖에 없는데? case가 2개임.
그리고 generate된 Member의 id 등, 다른 부가 정보도 필요할 텐데?

==> 이래서 사용한다. + 결과를 일률적으로 통일해서 관리

 */
