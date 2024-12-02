package com.koreait.surl_project_11.global.exceptionHandlers;

import com.koreait.surl_project_11.global.exceptions.GlobalException;
import com.koreait.surl_project_11.global.rq.Rq;
import com.koreait.surl_project_11.global.rsData.RsData;
import com.koreait.surl_project_11.standard.dto.Empty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

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

    //아래는 명시적으로 처리하지 않은 넓은 범위의 Exception을 처리하기 위한 코드
    //기타이기 때문에 원래 모양대로 내버려둔다 ==> 상정한 exception이 아니면 print trace(예외 발생에 대한 자세한 경위)를 보는게 좋으니깐.
    private Rq rq;

    @Autowired
    public GlobalExceptionHandler(Rq rq) {
        this.rq = rq;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("resultCode", "500-1");
        body.put("statusCode", 500);
        body.put("msg", ex.getLocalizedMessage());
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        body.put("data", data);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        data.put("trace", sw.toString().replace("\t", "    ").split("\\r\\n"));
        String path = rq.getCurrentUrlPath();
        data.put("path", path);
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(GlobalException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) //이걸 넣어주면 postman 테스트에서 결과창에 상단에 뜨는 result 자체를 400으로 바꿀 수 있다
    @ResponseBody
    //에러를 표현할 때는 데이터를 담을 필요가 없기 때문에 딱 code랑 msg만 담는 rsdata를 보내줄 수 있도록 한다.
    public ResponseEntity<RsData<Empty>> handlerException(GlobalException ex) {

        RsData<Empty> rsData = ex.getRsData();

//        log.debug("handleException 2");
        //이렇게 하면 status를 RsData를 활용한 것으로 바꿔줄 수 있다. ==> 내가 RsData에서 설정한 오류코드로 커스텀 가능.
        //아래가 최종 형태 => 성공과 실패의 양식을 통일
        return ResponseEntity
                .status(rsData.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(rsData);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<RsData<Empty>> handlerException(MethodArgumentNotValidException ex) {

        String resultCode = "400-" + ex.getBindingResult().getFieldError().getCode();
        String msg = ex.getBindingResult().getFieldError().getDefaultMessage();

        //한번 감싸서 내가 원래 쓰던 선호하던 모양의 exception에 넘겨주기
        return handlerException(
                new GlobalException(
                        resultCode,
                        msg
                )
        );
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

/*
- join 관련한 2가지 Handling
    - GlobalException 처리
    - MethodArgumentNotValidException 처리
      - MethodArgumentNotValidException 핸들러 추가
      - 해당 핸들러에서 GlobalException 형태로 변형
    - 응답의 양식이 같음
      - 양식: resultCode, statusCode, msg, data
      - 케이스
        - 성공할 경우
        - 실패할 경우 1: GlobalException 발생
        - 실패할 경우 2: MethodArgumentNotValidException 발생
==> client App에서 보여질때, 일관된 양식일 수록 에러를 파악하기 편하므로 통일해주려 하는 것

- 기타 예외 핸들러
  - trace : 예외에 대한 자세한 발생 경위
  - path: URL
- rq
  - rq는 요청과 응답 쌍에 대한 추상화된 레이어
  - rq는 내부적으로 요청(HttpServletRequest)과 응답(HttpServletResponse)에 접근하고 다룰 수 있다
- 기타 예외에 대한 처리를 하지 않으면 기본 양식과 동일한 양식으로 오류가 출력된다
 */
