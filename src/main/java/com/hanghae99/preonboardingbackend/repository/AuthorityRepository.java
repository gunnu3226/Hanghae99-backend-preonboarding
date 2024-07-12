package com.hanghae99.preonboardingbackend.repository;

import com.hanghae99.preonboardingbackend.model.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
    Authority findByAuthorityName(final String authorityName);
}
