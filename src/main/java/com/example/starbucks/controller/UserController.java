package com.example.starbucks.controller;

import com.example.starbucks.dto.ApiResponse;
import com.example.starbucks.model.UserCustom;
import com.example.starbucks.service.UserDetailServiceImpl;
import com.example.starbucks.service.UserService;
import com.example.starbucks.status.ResponseStatus;
import com.example.starbucks.status.ResultStatus;
import com.example.starbucks.token.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @Autowired
    UserDetailServiceImpl userDetailService;


    // <?>: java에서의 wildcard => (TS에서의 any와 같다고 생각하면 됨)
    public ApiResponse<?> validateApiResponse(ResultStatus status){
        ResponseStatus resultStatus =ResultStatus.FAIL.equals(status) ? ResponseStatus.FAIL : ResponseStatus.SUCCESS;
        String message = ResultStatus.FAIL.equals(status) ? "실패" : "성공";
        return new ApiResponse(resultStatus, message,null);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody UserCustom userCustom) {
        // userId, pwd, nickname
        // 아이디 유효성검사, 중복조회, 비밀번호 암호화
        userCustom.setPwd(passwordEncoder.encode(userCustom.getPwd()));

        // 저장
        userService.saveUser(userCustom);
        ApiResponse apiResponse = new ApiResponse(ResponseStatus.SUCCESS, "성공", null);
        return ResponseEntity.ok(apiResponse);
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody UserCustom userCustom){
        // id와 pwd를 체크해주는 security 라이브러리를 활용해야함
        try {
            // UserDetail에서 제공한 클래스를 활용하여
            // DB에서 쉽게 해당 id 가져오는 역할
            UserDetails userDetails = userDetailService.loadUserByUsername(userCustom.getUserId());

            // 가져온 아이디가 null이거나 post로 보낸 비밀번호가 일치하지 않으면 if 실행
            if(userDetails == null || !passwordEncoder.matches(userCustom.getPwd(), userDetails.getPassword())){
                throw new BadCredentialsException("아이디와 비밀번호가 일치하지 않습니다.");
            }

            // 성공
            // 토큰 발급
            String token = JwtUtil.generateToken(userCustom);

            // response header
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", "Bearer " + token);

            // 출입증 객체
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            // 출입관리부에 작성
            // 카카오톡[]    getContext() -> 전역으로 쓰이는 변수에 접근하는 메서드
            SecurityContextHolder.getContext().setAuthentication(authentication);




            // response body
            ApiResponse apiResponse = new ApiResponse(ResponseStatus.SUCCESS, "로그인 성공", token);
            return ResponseEntity.ok().headers(httpHeaders).body(apiResponse);


        } catch (UsernameNotFoundException | BadCredentialsException e){
            System.out.println("그런 아이디 없음");
            ApiResponse apiResponse = new ApiResponse(ResponseStatus.UNAUTHORIZED, "로그인 실패", null);
            return ResponseEntity.ok(apiResponse);
        }



    }




}


