package com.hanghae99.preonboardingbackend.controller;

import com.hanghae99.preonboardingbackend.config.PasswordUtil;
import com.hanghae99.preonboardingbackend.config.jwt.TokenProvider;
import com.hanghae99.preonboardingbackend.dto.ResponseDTO;
import com.hanghae99.preonboardingbackend.dto.request.LoginRequestDTO;
import com.hanghae99.preonboardingbackend.dto.request.SignupRequestDTO;
import com.hanghae99.preonboardingbackend.dto.response.TokenResponseDTO;
import com.hanghae99.preonboardingbackend.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseDTO<Void>> signup(
        @Valid @RequestBody SignupRequestDTO signupRequestDTO
    ) {
        log.info("test");
        userService.signup(signupRequestDTO.username(), signupRequestDTO.password());

        return ResponseEntity.ok(
            new ResponseDTO<>(HttpStatus.OK.value(), "회원가입성공")
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<Void>> login(
        HttpServletResponse response,
        @RequestBody LoginRequestDTO loginRequestDTO
    ) {
        TokenResponseDTO responseDTO = userService.login(
            loginRequestDTO.username(),
            loginRequestDTO.password()
        );
        response.addHeader(TokenProvider.AUTHORIZATION_HEADER, responseDTO.accessToken());
        return ResponseEntity.ok(
            new ResponseDTO<>(HttpStatus.OK.value(), "로그인성공")
        );
    }
}
