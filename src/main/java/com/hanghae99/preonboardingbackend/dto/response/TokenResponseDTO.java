package com.hanghae99.preonboardingbackend.dto.response;

public record TokenResponseDTO(
    String accessToken,
    String refreshToken
) {

}
