package com.hanghae99.preonboardingbackend.service;

import com.hanghae99.preonboardingbackend.config.PasswordUtil;
import com.hanghae99.preonboardingbackend.config.jwt.TokenProvider;
import com.hanghae99.preonboardingbackend.config.jwt.UserDetailsImpl;
import com.hanghae99.preonboardingbackend.dto.response.TokenResponseDTO;
import com.hanghae99.preonboardingbackend.exception.user.ExistUsernameException;
import com.hanghae99.preonboardingbackend.model.entity.Authority;
import com.hanghae99.preonboardingbackend.model.entity.User;
import com.hanghae99.preonboardingbackend.repository.UserRepository;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.hibernate.result.UpdateCountOutput;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final PasswordUtil passwordUtil;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final String ROLE_USER = "USER";

    public UserService(UserRepository userRepository, TokenProvider tokenProvider,
        PasswordUtil passwordUtil, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.userRepository = userRepository;
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

    public void signup(final String username, final String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new ExistUsernameException("해당 username은 이미 존재합니다.");
        }
        String encodedPassword = passwordUtil.encode(password);
        userRepository.save(User.builder()
            .username(username)
            .password(encodedPassword)
            .authorities(Set.of(new Authority(ROLE_USER)))
            .build());
    }
}
