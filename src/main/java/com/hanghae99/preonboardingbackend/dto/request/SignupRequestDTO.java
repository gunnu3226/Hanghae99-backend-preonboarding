package com.hanghae99.preonboardingbackend.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SignupRequestDTO(

    @NotBlank(message = "회원이름은 필수 입력 값입니다")
    String username,

    @NotBlank(message = "비밀번호를 입력하세요")
    String password
) {

}
