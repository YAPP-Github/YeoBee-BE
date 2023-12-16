package com.example.yeobee.domain.user.service;

import com.example.yeobee.domain.user.entity.RoleType;
import com.example.yeobee.domain.user.entity.User;
import com.example.yeobee.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getOrCreateUserByUserId(String userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.orElseGet(() -> userRepository.save(User.builder()
                .id(userId)
                .roleType(RoleType.USER)
                .build()));
    }
}
