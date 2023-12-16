package com.example.yeobee.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
public class RefreshToken {
    @Id
    private String userId;
    private String refreshToken;
}
