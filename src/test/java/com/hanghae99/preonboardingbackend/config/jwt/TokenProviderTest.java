package com.hanghae99.preonboardingbackend.config.jwt;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.hanghae99.preonboardingbackend.model.entity.Authority;
import com.hanghae99.preonboardingbackend.model.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@ExtendWith(MockitoExtension.class)
class TokenProviderTest {

    @InjectMocks
    private TokenProvider tokenProvider;

    private Key key;
    private String secretKey = "a71b8a7edfd427da96e2460b24b6eebe";

    private String BEARER_PREFIX = "Bearer ";
    private final String USER_ID = "userId";
    private final String AUTHORIZATION_KEY = "auth";
    private final Authority authority = new Authority("ROLE_USER");

    private final User TEST_USER = User.builder()
        .userId(1L)
        .username("testUser")
        .authorities(Set.of(authority))
        .build();

    @BeforeEach
    public void setup() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    @Test
    @Order(1)
    @DisplayName("AccessToken 생성 성공")
    void generateAccessToken() {
        //when
        String token = tokenProvider.createAccessToken(
            TEST_USER.getUserId(),
            TEST_USER.getUsername(),
            TEST_USER.getAuthorities()
        );

        //then
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token.replace(BEARER_PREFIX, ""))
            .getBody();

        assertNotNull(token);
        Assertions.assertThat(claims.getSubject()).isEqualTo(TEST_USER.getUsername());
        Assertions.assertThat(claims.get(USER_ID)).isEqualTo(TEST_USER.getUserId());
        Assertions.assertThat(claims.get(AUTHORIZATION_KEY)).isEqualTo(TEST_USER.getAuthorities());
    }

    @Test
    @DisplayName("RefreshToken 생성 성공")
    void generateRefreshToken() {
        //When
        String token = tokenProvider.createRefreshToken();

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
            String token = tokenProvider.createAccessToken(
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
        void 유효하지않은_토큰_검증() {
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
                    .claim(USER_ID, TEST_USER.getUserId())
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

