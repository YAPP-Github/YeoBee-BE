package com.example.yeobee.core.user.domain;

import com.example.yeobee.common.entity.BaseEntity;
import com.example.yeobee.core.auth.domain.AuthProvider;
import com.example.yeobee.core.tripUser.domain.TripUser;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private String profileImage;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AuthProvider> authProviderList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<TripUser> tripUserList;

    public User(AuthProvider authProvider) {
        addAuthProvider(authProvider);
    }

    public void addAuthProvider(AuthProvider authProvider) {
        authProviderList.add(authProvider);
        if (authProvider.getUser() == null) {
            authProvider.setUser(this);
        }
    }
}
