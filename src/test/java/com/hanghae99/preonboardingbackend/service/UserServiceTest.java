package com.hanghae99.preonboardingbackend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.hanghae99.preonboardingbackend.config.PasswordUtil;
import com.hanghae99.preonboardingbackend.config.jwt.TokenProvider;
import com.hanghae99.preonboardingbackend.dto.response.SignupResponseDTO;
import com.hanghae99.preonboardingbackend.exception.user.ExistUsernameException;
import com.hanghae99.preonboardingbackend.model.entity.Authority;
import com.hanghae99.preonboardingbackend.model.entity.User;
import com.hanghae99.preonboardingbackend.repository.AuthorityRepository;
import com.hanghae99.preonboardingbackend.repository.UserRepository;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthorityRepository authorityRepository;
    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private PasswordUtil passwordUtil;
    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    private final Long TEST_USER_ID = 1L;
    private final String TEST_USERNAME = "testuser";
    private final String TEST_PASSWORD = "testpassword";
    private final String TEST_NICKNAME = "nickname";
    private final Authority TEST_AUTHORITY = new Authority("USER");
    private final Set<Authority> TEST_AUTHORITIES = new HashSet<>();

    private final User TEST_USER = User.builder()
        .userId(TEST_USER_ID)
        .username(TEST_USERNAME)
        .nickname(TEST_NICKNAME)
        .authorities(TEST_AUTHORITIES)
        .build();

    @Nested
    @DisplayName("회원가입")
    class signup {

        @Test
        @DisplayName("회원가입_성공")
        void 회원가입_성공() {
            //given
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
            when(passwordUtil.encode(anyString())).thenReturn(TEST_PASSWORD);
            when(userRepository.save(any(User.class))).thenReturn(TEST_USER);
            when(authorityRepository.findByAuthorityName(anyString())).thenReturn(TEST_AUTHORITY);

            //when
            SignupResponseDTO responseDTO = userService.signup(
                TEST_USERNAME,
                TEST_PASSWORD,
                TEST_NICKNAME
            );

            //then
            assertThat(responseDTO.username()).isEqualTo(TEST_USERNAME);
            assertThat(responseDTO.nickname()).isEqualTo(TEST_NICKNAME);
            assertThat(responseDTO.authorities().size()).isEqualTo(1);
            assertTrue(responseDTO.authorities().contains(TEST_AUTHORITY));
        }

        @Test
        @DisplayName("회원가입_실패_중복_아이디")
        void 회원가입_실패_중복아이디() {
            // given
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(TEST_USER));

            // when & then
            assertThrows(ExistUsernameException.class, () -> {
                userService.signup(TEST_USERNAME, TEST_PASSWORD, TEST_NICKNAME);
            });
        }
    }
}
