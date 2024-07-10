package com.hanghae99.preonboardingbackend.config.jwt;

import com.hanghae99.preonboardingbackend.model.entity.Authority;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.security.Key;

@Configuration
@Slf4j
public class TokenProvider implements InitializingBean {
    private static final String BEARER_PREFIX = "Bearer ";
    public static final String USER_ID = "userId";
    private static final String AUTHORITIES_KEY = "authorities";
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.token-validity-in-milliseconds}")
    private long tokenValidityInMs;
    @Value("${jwt.refresh-token-validity-in-milliseconds}")
    private long refreshTokenValidityInMs;

    private Key key;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String createAccessToken(
        final Long userId,
        final String username,
        final Set<Authority> authorities
    ) {
        Date date = new Date();

        return BEARER_PREFIX +
            Jwts.builder()
                .setSubject(username)
                .claim(USER_ID, userId)
                .claim(AUTHORITIES_KEY, authorities)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + tokenValidityInMs))
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public String createRefreshToken() {
        Date date = new Date();

        return BEARER_PREFIX +
            Jwts.builder()
                .setExpiration(new Date(date.getTime() + refreshTokenValidityInMs))
                .signWith(key, signatureAlgorithm)
                .compact();
    }
}
