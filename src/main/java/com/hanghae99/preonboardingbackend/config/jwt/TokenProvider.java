package com.hanghae99.preonboardingbackend.config.jwt;

import com.hanghae99.preonboardingbackend.model.entity.Authority;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Configuration
@Slf4j
public class TokenProvider implements InitializingBean {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String USER_ID = "userId";
    private static final String AUTHORITIES_KEY = "authorities";
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.token-validity-in-milliseconds}")
    private long accessTokenValidityInMs;
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
                .setExpiration(new Date(date.getTime() + accessTokenValidityInMs))
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

    public boolean validateToken(final String token) {
        String parseToken = parseToken(token);
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(parseToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty", e);
        }
        return false;
    }

    public Authentication createAuthentication(final String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        UserDetails userDetails = getUserDetailsFromToken(claims);
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        return new UsernamePasswordAuthenticationToken(
            userDetails, null, authorities);
    }

    private UserDetails getUserDetailsFromToken(
        final Claims claims
    ) {
        Long userId = ((Number) claims.get(USER_ID)).longValue();
        String username = claims.getSubject();
        //맘대로 캐스팅 했다가 타입 안맞는 경우가 생길 수 있을까?
        Set<Authority> authorities = (Set<Authority>) claims.get(AUTHORITIES_KEY);
        return new UserDetailsImpl(userId, username, authorities);
    }

    private Claims parseClaims(String accessToken) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(accessToken)
            .getBody();
    }

    private String parseToken(final String token) {
        return token.replace(BEARER_PREFIX,"");
    }

}
