package com.example.yeobee.domain.user.repository;

import com.example.yeobee.domain.user.entity.AuthProvider;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthProviderRepository extends JpaRepository<AuthProvider, Long> {

    Optional<AuthProvider> findBySocialLoginId(String socialLoginId);
}
