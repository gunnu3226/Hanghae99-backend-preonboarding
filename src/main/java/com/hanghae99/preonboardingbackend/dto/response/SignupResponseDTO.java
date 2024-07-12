package com.hanghae99.preonboardingbackend.dto.response;

import com.hanghae99.preonboardingbackend.model.entity.Authority;
import com.hanghae99.preonboardingbackend.model.entity.User;
import java.util.Set;

public record SignupResponseDTO(
    String username,
    String nickname,
    Set<Authority> authorities
) {

    public static SignupResponseDTO from(final User user) {
        return new SignupResponseDTO(
            user.getUsername(),
            user.getNickname(),
            user.getAuthorities()
        );
    }
}
