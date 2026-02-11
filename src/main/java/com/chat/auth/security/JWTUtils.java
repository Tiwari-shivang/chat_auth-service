package com.chat.auth.security;

import com.chat.auth.models.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;

@Component
public class JWTUtils {
    private final String secretVal = "my_secret_key_long_enough_usable";
    private final SecretKey secret = Keys.hmacShaKeyFor(secretVal.getBytes(StandardCharsets.UTF_8));
    public String buildToken(Users user){
        HashMap<String, String> claims = new HashMap<>();
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());
        claims.put("userName", user.getUsername());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole().toString());
        claims.put("gender", user.getGender().name());
        claims.put("isActive", user.getIsActive().toString());
        claims.put("isVerified", user.getIsVerified().toString());
        return Jwts.builder().signWith(secret).subject(user.getEmail()).claim("user", claims).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)).compact();
    }

    public String extractEmail(String token){
        return Jwts.parser().verifyWith(secret).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public Claims getClaims(String token){
        return Jwts.parser().verifyWith(secret).build().parseSignedClaims(token).getPayload();
    }

    public Boolean validateToken(String token, Users user){
        Claims claims = getClaims(token);
        if(claims.getSubject().matches(user.getEmail())){
            return !claims.getExpiration().before(new Date());
        }
        return false;
    }
}
