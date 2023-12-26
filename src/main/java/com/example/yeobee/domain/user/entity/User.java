package com.example.yeobee.domain.user.entity;

import com.example.yeobee.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    private String id = UUID.randomUUID().toString();
    @Column(unique = true)
    private String socialLoginId;
    private LoginProvider loginProvider;
    private RoleType roleType;
    private String appleRefreshToken;

    @Builder
    public User(String socialLoginId, LoginProvider loginProvider, RoleType roleType, String appleRefreshToken) {
        this.socialLoginId = socialLoginId;
        this.loginProvider = loginProvider;
        this.roleType = roleType;
        this.appleRefreshToken = appleRefreshToken;
    }
}
