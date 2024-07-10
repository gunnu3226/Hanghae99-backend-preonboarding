package com.hanghae99.preonboardingbackend.config.jwt;

import com.hanghae99.preonboardingbackend.model.entity.Authority;
import com.hanghae99.preonboardingbackend.model.entity.User;
import java.util.Set;

public interface JwtTest {

    Authority AUTHORITY_USER = new Authority("ROLE_USER");
    User TEST_USER = new User(1L, "testUser", "password", "nickname", true, Set.of(AUTHORITY_USER));

    String MEMBER_ID = "memberId";
    String AUTHORIZATION_KEY = "auth";
    String BEARER_PREFIX = "Bearer ";
}
