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

/*
- return은 해당 statement가 포함된 함수만 끝내지만,
- throw는 예외처리를 호출자에서 해주지 않으면 해당 쓰레드 전체가 끝난다.
  - 즉 해당 HTTP 요청에 대한 처리 자체가 끝난다
- 예외를 발생시키고 예외처리를 하지 않으면 스프링부트가 매뉴얼대로 오류를 처리한다(==whitelabel error page)
- @ControllerAdvice 클래스에 @ExceptionHandler 메서드를 만들면 오류에 대한 처리를 가로챈다
- @ExceptionHandler 메서드가 여러개라면 해당 예외상황에 더 적절한 메서드가 알아서 선택된다
- @ExceptionHandler 메서드가 존재해도 예외상황과 맞는 Handler가 아무것도 없다면 메서드는 작동되지 않음
 */
