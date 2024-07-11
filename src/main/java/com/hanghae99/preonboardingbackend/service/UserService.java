package com.hanghae99.preonboardingbackend.service;

import com.hanghae99.preonboardingbackend.config.PasswordUtil;
import com.hanghae99.preonboardingbackend.config.jwt.TokenProvider;
import com.hanghae99.preonboardingbackend.config.jwt.UserDetailsImpl;
import com.hanghae99.preonboardingbackend.dto.response.TokenResponseDTO;
import com.hanghae99.preonboardingbackend.exception.user.ExistUsernameException;
import com.hanghae99.preonboardingbackend.model.entity.Authority;
import com.hanghae99.preonboardingbackend.model.entity.User;
import com.hanghae99.preonboardingbackend.repository.AuthorityRepository;
import com.hanghae99.preonboardingbackend.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final TokenProvider tokenProvider;
    private final PasswordUtil passwordUtil;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final String ROLE_USER = "ROLE_USER";

    public UserService(UserRepository userRepository, AuthorityRepository authorityRepository, TokenProvider tokenProvider,
        PasswordUtil passwordUtil, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.tokenProvider = tokenProvider;
        this.passwordUtil = passwordUtil;
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
            userDetails.getSetAuthorities()
        );
        String refreshToken = tokenProvider.createRefreshToken();

        return new TokenResponseDTO(accessToken, refreshToken);
    }

    @Transactional
    public void signup(final String username, final String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new ExistUsernameException("해당 username은 이미 존재합니다.");
        }
        String encodedPassword = passwordUtil.encode(password);
        User savedUser = userRepository.save(User.builder()
            .username(username)
            .password(encodedPassword)
            .build());

        Authority authority = authorityRepository.findByAuthorityName(ROLE_USER);
        savedUser.addAuthorities(authority);
    }

    @Transactional
    public void signupNoRole(final String username, final String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new ExistUsernameException("해당 username은 이미 존재합니다.");
        }
        String encodedPassword = passwordUtil.encode(password);
        userRepository.save(User.builder()
            .username(username)
            .password(encodedPassword)
            .build());
    }
}
