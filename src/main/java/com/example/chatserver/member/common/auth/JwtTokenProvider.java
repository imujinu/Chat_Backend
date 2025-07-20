package com.example.chatserver.member.common.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

@Component
public class JwtTokenProvider {

    private final String secretKey;
    private final int expiration;
    private Key SECRET_KEY;
    public JwtTokenProvider(@Value("${jwt.secretKey}") String secretKey, @Value("${jwt.expiration}")int expiration, Key SECRET_KEY) {
        this.expiration = expiration;
        this.secretKey = secretKey;
        this.SECRET_KEY = new SecretKeySpec(java.util.Base64.getDecoder().decode(secretKey), SignatureAlgorithm.HS512.getJcaName());
    }
}
