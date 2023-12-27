package com.example.yeobee.domain.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AuthProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(unique = true)
    private String socialLoginId;
    @Enumerated(EnumType.STRING)
    private LoginProvider loginProvider;
    private String appleRefreshToken;

    @Builder
    public AuthProvider(User user, String socialLoginId, LoginProvider loginProvider, String appleRefreshToken) {
        this.user = user;
        this.socialLoginId = socialLoginId;
        this.loginProvider = loginProvider;
        this.appleRefreshToken = appleRefreshToken;
    }
}
