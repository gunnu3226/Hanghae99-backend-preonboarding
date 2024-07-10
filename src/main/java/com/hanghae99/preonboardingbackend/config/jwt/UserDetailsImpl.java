package com.hanghae99.preonboardingbackend.config.jwt;

import com.hanghae99.preonboardingbackend.model.entity.Authority;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class UserDetailsImpl implements UserDetails {

    private Long userId;
    private String username;
    private String password;
    private Set<Authority> authorities;

    public UserDetailsImpl(
        final Long userId,
        final String username,
        final Set<Authority> authorities
    ) {
        this.userId = userId;
        this.username = username;
        this.authorities = authorities;
    }

    public UserDetailsImpl(Long userId, String username, String password,
        Set<Authority> authorities) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public Set<Authority> getSetAuthorities() {
        return authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities.stream()
            .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
            .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return null;
    }
}
