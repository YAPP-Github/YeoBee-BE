package com.example.yeobee.core.trip.domain;

import com.example.yeobee.core.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class TripUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private TripUserState state;

    @Enumerated(EnumType.STRING)
    private TripUserDefaultProfileImageType profileImageType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    public TripUser(User user, Trip trip) {
        this.state = TripUserState.CONNECTED;
        this.profileImageType = TripUserDefaultProfileImageType.IMAGE0; // TODO: 유저 프로필 이미지 무조건 설정 되도록
        this.user = user;
        this.trip = trip;
    }

    public TripUser(String name, TripUserDefaultProfileImageType profileImageType, Trip trip) {
        this.name = name;
        this.state = TripUserState.UNCONNECTED;
        this.profileImageType = profileImageType;
        this.trip = trip;
    }

    public String getProfileImageUrl() {
        return (user == null || user.getProfileImageUrl() == null) ?
            profileImageType.getImageUrl() : user.getProfileImageUrl();
    }

    public String getName() {
        return (user == null) ? name : user.getNickname();
    }

    public Long getUserId() {
        return (user == null) ? null : user.getId();
    }
}
