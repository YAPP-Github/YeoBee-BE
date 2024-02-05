package com.example.yeobee.core.trip.domain;

import com.example.yeobee.core.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
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

    public String getProfileImageUrl() {
        return (user == null || user.getProfileImageUrl() == null) ?
            profileImageType.getImageUrl() : user.getProfileImageUrl();
    }

    public String getTripUserName() {
        return (user == null) ? name : user.getNickname();
    }

    public Long getUserId() {
        return (user == null) ? null : user.getId();
    }
}
