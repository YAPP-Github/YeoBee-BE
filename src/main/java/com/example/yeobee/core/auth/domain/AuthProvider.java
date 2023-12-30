package com.example.yeobee.core.auth.domain;

import com.example.yeobee.core.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(unique = true)
    private String socialLoginId;

    @Enumerated(EnumType.STRING)
    private AuthProviderType type;

    @Setter
    private String appleRefreshToken;

    public AuthProvider(String socialLoginId, AuthProviderType type) {
        this.socialLoginId = socialLoginId;
        this.type = type;
    }
}
