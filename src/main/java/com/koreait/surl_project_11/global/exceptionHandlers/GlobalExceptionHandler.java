package com.koreait.surl_project_11.global.exceptionHandlers;

import com.koreait.surl_project_11.global.exceptions.GlobalException;
import com.koreait.surl_project_11.global.rsData.RsData;
import com.koreait.surl_project_11.standard.dto.Empty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    //이렇게 해두면 좀더 '범위가 좁은'(특정적인) exception의 handler가 우선적으로 움직인다.
    //아래의 경우를 예시로 들면, GlobalException이면 GlobalExceptionHandler가 실행되고 그 외의 예외에서는 Exception의 handler가 실행된다.
    //Exception의 계열을 따진다!
//    @ExceptionHandler(Exception.class)
//    @ResponseBody
//    public String handlerException(Exception ex) {
//        log.debug("handleException 1");
//        return ex.getMessage();
//    }
    @ExceptionHandler(GlobalException.class)
    //@ResponseStatus(HttpStatus.BAD_REQUEST) //이걸 넣어주면 postman 테스트에서 결과창에 상단에 뜨는 result 자체를 400으로 바꿀 수 있다
    @ResponseBody
    public ResponseEntity<String> handlerException(GlobalException ex) {
        RsData<Empty> rsData = ex.getRsData();

        rsData.getStatusCode();

//        log.debug("handleException 2");
        //이렇게 하면 status를 RsData를 활용한 것으로 바꿔줄 수 있다. ==> 내가 RsData에서 설정한 오류코드로 커스텀 가능.
        return ResponseEntity.status(rsData.getStatusCode()).body(rsData.getMsg());
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

/*
2xx 성공
4xx 실패 => 클라이언트 때문에 실패
5xx 실패 => 서버 때문에 실패

@ResponseStatus(HttpStatus.BAD_REQUEST) : 400으로 고정
- ResponseEntity<String>으로 리턴 : 상황에 따라서 다른 상태코드 지정 가능
*/
