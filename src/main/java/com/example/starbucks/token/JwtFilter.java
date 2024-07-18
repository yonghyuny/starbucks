package com.example.starbucks.token;

import com.example.starbucks.service.UserDetailServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    // 토큰 검사안하고 통과시키는 변수
    private List<String> excludeURLS = Arrays.asList("/api/v1/user/login", "/api/v1/user/register");

    public JwtFilter(JwtUtil jwtUtil, UserDetailServiceImpl userDetailService) {
        this.jwtUtil = jwtUtil;
        this.userDetailService = userDetailService;
    }
    // 스프링부트에 요청하면 무조건 걸치는 입구
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 토큰검사 안하고 통과 하는 로직
        if(excludeURLS.stream().anyMatch(uri -> uri.equals(request.getRequestURI()))){
            filterChain.doFilter(request,response);
            return;
        }



        String authorizationHeader = request.getHeader("Authorization");
        boolean headerIsEmpty = authorizationHeader==null;
        boolean doNotHavehasBearer = !authorizationHeader.startsWith("Bearer");

        //token이 없을 때
        if(headerIsEmpty || doNotHavehasBearer){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authorization header is invalid");
            return;
        }


        String token = authorizationHeader.substring(7);
        // token 유효기간 or 싸인불일치
        System.out.println(token);
        if(!jwtUtil.validToken(token)){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token is invalid");
            return;
        }

        // token 괜춘
        String name = jwtUtil.extractToken(token).getSubject();
        // 이중보안 db에서 확인 작업
        UserDetails userDetails = userDetailService.loadUserByUsername(name);
        // 출입부 재작성[Security framework에서 필수로 하라고함]
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request,response);
    }



}
