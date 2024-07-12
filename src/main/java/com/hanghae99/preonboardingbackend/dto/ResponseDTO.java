package com.hanghae99.preonboardingbackend.dto;

public record ResponseDTO<T>(
    int statusCode,
    String message,
    T data
) {

    public ResponseDTO(final int statusCode,final String message) {
        this(statusCode, message, null);
    }

    public ResponseDTO(final int statusCode,final String message,final T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }
}
