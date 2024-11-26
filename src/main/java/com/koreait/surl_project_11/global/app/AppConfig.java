package com.koreait.surl_project_11.global.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//Configuration이 붙은 클래스에서는 Bean을 등록할 수 있다. (SecurityConfig에 넣어도 동작은 함.)
//다만, 위치에 따라 Bean이 등록되는 순서가 차이가 있을 수 있으므로 분리해두는게 좋다.
@Configuration
public class AppConfig {

    //비밀번호 암호화를 위한 Bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //보안성이 매우 뛰어남! => 암호가 동일하게 1234여도 모두 다르게 인코딩되는 것을 확인할 수 있다.
    }
}
