package com.hanghae99.preonboardingbackend.service;

import com.hanghae99.preonboardingbackend.config.jwt.TokenProvider;
import com.hanghae99.preonboardingbackend.config.jwt.UserDetailsImpl;
import com.hanghae99.preonboardingbackend.dto.response.TokenResponseDTO;
import com.hanghae99.preonboardingbackend.model.entity.Authority;
import com.hanghae99.preonboardingbackend.repository.UserRepository;
import java.util.Set;
import org.hibernate.result.UpdateCountOutput;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public UserService(UserRepository userRepository, TokenProvider tokenProvider,
        AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    public TokenResponseDTO login(final String username, final String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = authenticationManagerBuilder.getObject()
            .authenticate(authenticationToken);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String accessToken = tokenProvider.createAccessToken(
            userDetails.getUserId(),
            userDetails.getUsername(),
            (Set<Authority>) userDetails.getAuthorities());
        String refreshToken = tokenProvider.createRefreshToken();

        return new TokenResponseDTO(accessToken, refreshToken);
    }
}
