package com.sbnz.ibar.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenUtils {

    @Value("ibar")
    private String APP_NAME;

    @Value("somesecret")
    public String SECRET;

    @Value("1800000") // 5h
    private int EXPIRES_IN;

    @Value("Authorization")
    private String AUTH_HEADER;

    private final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuer(this.APP_NAME)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + this.EXPIRES_IN))
                .signWith(this.SIGNATURE_ALGORITHM, this.SECRET).compact();
    }

    public boolean validateToken(UserDetails user, String token) {
        String email = this.getUsername(token);
        return email != null && email.equals(user.getUsername());
    }

    public String getUsername(String token) {
        return Jwts.parser()
                .setSigningKey(this.SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
