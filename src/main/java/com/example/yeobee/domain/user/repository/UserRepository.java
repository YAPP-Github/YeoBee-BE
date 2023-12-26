package com.example.yeobee.domain.user.repository;

import com.example.yeobee.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findBySocialLoginId(String socialId);
}
