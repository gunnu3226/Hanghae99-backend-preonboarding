package com.hanghae99.preonboardingbackend.controller;

import com.hanghae99.preonboardingbackend.dto.ResponseDTO;
import com.hanghae99.preonboardingbackend.exception.user.ExistUsernameException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseDTO<String>> handleException(
        RuntimeException ex
    ) {
        return ResponseEntity.badRequest()
            .body(
                new ResponseDTO<>(
                    HttpStatus.BAD_REQUEST.value(),
                    ex.getMessage())
            );
    }

    @ExceptionHandler(ExistUsernameException.class)
    public ResponseEntity<ResponseDTO<String>> handleExistUsernameException(
        RuntimeException ex
    ) {
        return ResponseEntity.badRequest()
            .body(
                new ResponseDTO<>(
                    HttpStatus.BAD_REQUEST.value(),
                    ex.getMessage())
            );
    }
}
