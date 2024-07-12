package com.hanghae99.preonboardingbackend.exception.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae99.preonboardingbackend.dto.ResponseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        ResponseDTO responseDTO = new ResponseDTO(
            HttpStatus.UNAUTHORIZED.value(),
            "인증이 필요한 접근입니다"
        );
        String jsonResponse = new ObjectMapper().writeValueAsString(responseDTO);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(jsonResponse);
    }
}
