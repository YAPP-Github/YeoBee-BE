package com.example.yeobee.auth.jwt.securityUserDetails;

import com.example.yeobee.domain.user.entity.User;
import com.example.yeobee.domain.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SecurityUserDetailsService {

    private UserService userService;

    public UserDetails loadUserByUserId(String userId) {
        User user = userService.getOrCreateUserByUserId(userId);
        return new SecurityUserDetails(user);
    }
}
