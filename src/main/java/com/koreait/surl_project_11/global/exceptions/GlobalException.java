package com.koreait.surl_project_11.global.exceptions;

import com.koreait.surl_project_11.global.rsData.RsData;
import com.koreait.surl_project_11.standard.dto.Empty;
import lombok.Getter;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus
public class GlobalException extends RuntimeException {
    private final RsData<Empty> rsData;

    public GlobalException() {
        this("400-0", "에러");
    }

    public GlobalException(String msg) {
        this("400-0", msg);
    }

    public GlobalException(String resultCode, String msg) {
        super("resultCode=" + resultCode + ",msg=" + msg);
        this.rsData = RsData.of(resultCode, msg);
    }

    public static class E404 extends GlobalException {
        public E404() {
            super("404-0", "데이터를 찾을 수 없습니다.");
        }
    }
}