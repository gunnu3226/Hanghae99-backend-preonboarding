package com.hanghae99.preonboardingbackend.config.jwt;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TokenProviderTest implements JwtTest {

    @Autowired
    private TokenProvider tokenProvider;

    @Value("${jwt.secret}")
    private String secretKey;
    
    private Key key;

    @Value("${token-validity-in-milliseconds}")
    private long refreshTokenValidityInMs;

    @BeforeEach
    public void setup() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    @Test
    @Order(1)
    @DisplayName("AccessToken 생성 성공")
    void generateAccessToken() {
        //when
        String token = tokenProvider.createAccessToken(TEST_USER.getUsername());

        //then
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token.replace(BEARER_PREFIX, ""))
            .getBody();

        assertNotNull(token);
        Assertions.assertThat(claims.getSubject()).isEqualTo(TEST_USER.getUsername());
        Assertions.assertThat(claims.get(MEMBER_ID)).isEqualTo(TEST_USER.getUserId());
        Assertions.assertThat(claims.get(AUTHORIZATION_KEY)).isEqualTo(TEST_USER.getAuthorities());
    }

    @Test
    @DisplayName("RefreshToken 생성 성공")
    public void generateRefreshToken() {
        //When
        String token = tokenProvider.createRefreshToken(
            TEST_USER.getUserId(),
            TEST_USER.getUsername(),
            TEST_USER.getAuthorities()
        );

        //Then
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token.replace(BEARER_PREFIX, ""))
            .getBody();

        assertNotNull(token);

        // Check expiration
        Date now = new Date();
        Date expiration = claims.getExpiration();
        Assertions.assertThat(expiration).isAfter(now);
    }

    @Nested
    @DisplayName("토큰 검증")
    class 토큰검증 {

        @Test
        @DisplayName("유효한 토큰 검증")
        void 유효한_토큰_검증() {
            //given
            String token = tokenProvider.createRefreshToken(
                TEST_USER.getUserId(),
                TEST_USER.getUsername(),
                TEST_USER.getAuthorities()
            );

            //when
            boolean isValid = tokenProvider.validateToken(token);

            //then
            assertTrue(isValid);
        }

        @Test
        @DisplayName("유효하지 않은 토큰 검증")
        void 유효한_토큰_검증() {
            //given
            String token = "isNotValidToken";

            //when
            boolean isValid = tokenProvider.validateToken(token);

            //then
            assertFalse(isValid);
        }

        @Test
        @DisplayName("만료된 토큰 검증")
        void 만료된_토큰_검증() {
            //given
            Date now = new Date();
            Date expiration = new Date(now.getTime() - 1000);
             String token = BEARER_PREFIX + Jwts.builder()
                    .setSubject(TEST_USER.getUsername())
                    .claim(MEMBER_ID, TEST_USER.getUserId())
                    .claim(AUTHORIZATION_KEY, TEST_USER.getAuthorities())
                    .setExpiration(expiration)
                    .setIssuedAt(now)
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();

             //when
            boolean isValid = tokenProvider.validateToken(token);

            //then
            assertFalse(isValid);
        }
    }
}

