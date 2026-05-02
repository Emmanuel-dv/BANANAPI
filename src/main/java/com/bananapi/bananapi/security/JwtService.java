package com.bananapi.bananapi.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private static final String CLAVE_SECRETA = "YmFuYW5hcGktc3VwZXItc2VjcmV0LWtleS1kZXZlbG9wbWVudC0xMjM0NTY3ODkw";


    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(CLAVE_SECRETA);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                .signWith(getSigningKey())
                .compact();
    }
}
