package com.example.yeobee.domain.user.repository;

import com.example.yeobee.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findBySocialLoginId(String socialId);
}
