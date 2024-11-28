package com.example.demo.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.List;

@Component
public class JwtTokenProvider {

    private SecretKey jwtSecret = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // Kullanıcı adı ve roller ile JWT token'ı oluşturma
    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)  // Kullanıcı adı
                .claim("roles", roles)  // Roller
                .signWith(jwtSecret)  // Anahtar ile imzalanıyor
                .compact();  // Token oluşturuluyor
    }

    // Token'ı doğrulama
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Token'dan kullanıcı adı al
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(jwtSecret)
                .build().parseClaimsJws(token).getBody().getSubject();
    }

    // Token'dan roller bilgisini al
    public List<String> getRolesFromToken(String token) {
        return (List<String>) Jwts.parserBuilder().setSigningKey(jwtSecret)
                .build().parseClaimsJws(token).getBody().get("roles");
    }
}
