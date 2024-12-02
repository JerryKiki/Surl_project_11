package com.koreait.surl_project_11.global.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//Configuration이 붙은 클래스에서는 Bean을 등록할 수 있다. (SecurityConfig에 넣어도 동작은 함.)
//다만, 위치에 따라 Bean이 등록되는 순서가 차이가 있을 수 있으므로 분리해두는게 좋다.
@Configuration
public class AppConfig {

    //잭슨(복습: 자동으로 json 등으로 바꿔주는 번역기같은 놈)의 Bean을 받아오는 기능
    @Getter
    public static ObjectMapper objectMapper;
    @Getter
    private static String jwtSecretKey;
    @Getter
    private static long accessTokenExpirationSec;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    //비밀번호 암호화를 위한 Bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //보안성이 매우 뛰어남! => 암호가 동일하게 1234여도 모두 다르게 인코딩되는 것을 확인할 수 있다.
    }

    @Value("${custom.secret.jwt.secretKey}")
    public void setJwtSecretKey(String jwtSecretKey) {
        this.jwtSecretKey = jwtSecretKey;
    }

    @Value("${custom.accessToken.expirationSec}")
    public void setJwtSecretKey(long accessTokenExpirationSec) {
        this.accessTokenExpirationSec = accessTokenExpirationSec;
    }

}
