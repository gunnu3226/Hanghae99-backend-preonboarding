package com.hanghae99.preonboardingbackend.exception.user;

public class ExistUsernameException extends RuntimeException{

    public ExistUsernameException(String message) {
        super(message);
    }
}
