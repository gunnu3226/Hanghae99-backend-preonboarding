package com.hanghae99.preonboardingbackend.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SignupRequestDTO(

    @NotBlank(message = "회원이름은 필수 입력 값 입니다")
    String username,

    @NotBlank(message = "비밀번호는 필수 입력 값 입니다")
    String password,

    @NotBlank(message = "닉네임은 필수 입력 값 입니다")
    String nickname
) {

}
