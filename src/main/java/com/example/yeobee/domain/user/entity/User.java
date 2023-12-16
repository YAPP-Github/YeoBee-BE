package com.example.yeobee.domain.user.entity;

import com.example.yeobee.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
public class User extends BaseEntity {
    @Id
    private String id = UUID.randomUUID().toString();
    @Column(unique = true)
    private String socialLoginId;
    private LoginProvider loginProvider;
    private RoleType roleType;
    private String appleRefreshToken;
}
