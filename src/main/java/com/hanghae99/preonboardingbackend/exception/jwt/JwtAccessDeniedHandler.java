package com.hanghae99.preonboardingbackend.exception.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae99.preonboardingbackend.dto.ResponseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // HTTP 상태 코드 403 설정

        String jsonResponse = new ObjectMapper().writeValueAsString(
            new ResponseDTO<>(HttpStatus.FORBIDDEN.value(), "dd 거부되었습니다.")
        );

        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}
