package com.example.yeobee.core.user.domain;

import com.example.yeobee.common.entity.BaseEntity;
import com.example.yeobee.common.exception.BusinessException;
import com.example.yeobee.common.exception.ErrorCode;
import com.example.yeobee.core.trip.domain.TripUser;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE user SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private UserState state = UserState.ONBOARDING_NEEDED;

    private boolean isDeleted = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AuthProvider> authProviderList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<TripUser> tripUserList = new ArrayList<>();

    public User(AuthProvider authProvider) {
        addAuthProvider(authProvider);
    }

    public void addAuthProvider(AuthProvider authProvider) {
        authProviderList.add(authProvider);
        if (authProvider.getUser() == null) {
            authProvider.setUser(this);
        }
    }

    public void updateInfo(String nickname, String profileImage) {
        this.nickname = nickname;
        this.profileImageUrl = profileImage;
    }

    public AuthProvider getAuthProvider() {
        if (authProviderList.isEmpty()) {
            throw new BusinessException(ErrorCode.AUTH_PROVIDER_NOT_FOUND);
        }
        return authProviderList.get(0);
    }

    // Todo: state 관련 정책 정해지면 validation 추가
    public void updateUserState(UserState userState) {
        this.state = userState;
    }
}
