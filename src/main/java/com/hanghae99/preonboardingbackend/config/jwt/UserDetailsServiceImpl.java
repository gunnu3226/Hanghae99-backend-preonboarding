package com.hanghae99.preonboardingbackend.config.jwt;

import com.hanghae99.preonboardingbackend.model.entity.User;
import com.hanghae99.preonboardingbackend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow();
        return new UserDetailsImpl(
            user.getUserId(),
            user.getUsername(),
            user.getAuthorities()
        );
    }
}
