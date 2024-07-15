package com.example.starbucks;

// spring mvc -> controller
// spring jpa -> repository
// spring security -> securityConfig 설정

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity

public class SecurityConfig {

    // 패스워드 암호화 해주는 함수
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    // Http 요청을 처리 할 때, 적용되는 보안필터(인증, 권한부여, 세션)
    // HttpSecurity -> Http 보안 구성해주는 객체 [권한 부여 규칙, Form 태그 설정, ...]
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // csrf (Cross-site request forgery)(사이트간 요청 위조)
        http
                .csrf(x-> x.disable()) // csrf off 설정
                .sessionManagement(x -> x.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //세션기반 안함
                .authorizeHttpRequests(x ->
                        x.requestMatchers("**").permitAll() // 누구든지 우리의 starbucks 모든 파일("**") 접근 가능(permitAll)
                                .anyRequest().authenticated()); // 아무 request를 인증해야함
        // build 패턴
        /*수제 햄버거를 주문할때 빵이나 패티 등 속재료들은 주문하는 사람이 마음대로 결정된다.
        어느 사람은 치즈를 빼달라고 할수 있고 어느 사람은 토마토를 빼달라고 할수 있다.
        이처럼 선택적 속재료들을 보다 유연하게 받아 다양한 타입의 인스턴스를 생성할수 있어,
        클래스의 선택적 매개변수가 많은 상황에서 유용하게 사용된다.
*/


        return http.build();
    }



}
