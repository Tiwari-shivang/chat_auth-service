package com.chat.auth.helpers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class ShortLivedToken {
    private final String mySecretKet = "short_lived_token_secret_key_value_long_enough";
    private final SecretKey key = Keys.hmacShaKeyFor(mySecretKet.getBytes(StandardCharsets.UTF_8));
    public String createShortLivedToken(String email) {
        return Jwts.builder().signWith(key).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10)).subject(email).compact();
    }

    public Boolean validateShortLivedToken(String email, String token){
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        return claims.getSubject().matches(email) && !claims.getExpiration().before(new Date());
    }
}
