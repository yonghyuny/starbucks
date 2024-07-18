package com.example.starbucks.token;

import com.example.starbucks.model.UserCustom;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "YonghyunFrankKimYonghyunFrankKimYonghyunFrankKim";
    private static final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public static String generateToken(UserCustom userCustom){
        return Jwts
                .builder()
                .issuedAt(new Date(System.currentTimeMillis())) // 발급 시간
                .expiration(new Date(System.currentTimeMillis() + 1000 * 10 * 6))  // 만료시간
                .subject(userCustom.getUserId())
                .claim("userId", userCustom.getUserId())
                .signWith(key)
                .compact();
    }

    public static boolean validToken(String token){
       try{
        Jwts
                .parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
        return true;
       } catch (Exception e) {
           return false;
       }
    }

    public static Claims extractToken(String token){
        return Jwts
                .parser() // 해석함
                .verifyWith(key) // 키넘겨줌
                .build() // 빌드
                .parseSignedClaims(token) // 토큰 줄테니 claim 해석
                .getPayload(); // payload 넘겨줌

    }


}
