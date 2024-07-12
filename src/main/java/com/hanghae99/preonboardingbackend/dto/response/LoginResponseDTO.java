package com.hanghae99.preonboardingbackend.dto.response;

public record LoginResponseDTO(
    String accessToken,
    String refreshToken
) {

    public static LoginResponseDTO from(TokenResponseDTO responseDTO) {
        return new LoginResponseDTO(
            responseDTO.accessToken(),
            responseDTO.refreshToken()
        );
    }
}
