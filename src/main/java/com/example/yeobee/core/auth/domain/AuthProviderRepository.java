package com.example.yeobee.core.auth.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthProviderRepository extends JpaRepository<AuthProvider, Long> {

    Optional<AuthProvider> findBySocialLoginId(String socialLoginId);
}
