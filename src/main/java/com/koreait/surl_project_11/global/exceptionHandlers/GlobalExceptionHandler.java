package com.koreait.surl_project_11.global.exceptionHandlers;

import com.koreait.surl_project_11.global.exceptions.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    //이렇게 해두면 좀더 '범위가 좁은'(특정적인) exception의 handler가 우선적으로 움직인다.
    //아래의 경우를 예시로 들면, GlobalException이면 GlobalExceptionHandler가 실행되고 그 외의 예외에서는 Exception의 handler가 실행된다.
    //Exception의 계열을 따진다!
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String handlerException(Exception ex) {
        log.debug("handleException 1");
        return ex.getMessage();
    }
    @ExceptionHandler(GlobalException.class)
    @ResponseBody
    public String handlerException(GlobalException ex) {
        log.debug("handleException 2");
        return ex.getMessage();
    }
}
