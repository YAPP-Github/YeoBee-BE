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

    private String profileImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    public TripUser(User user, Trip trip) {
        this.state = TripUserState.CONNECTED;
        this.profileImageUrl = null;
        this.user = user;
        this.trip = trip;
    }

    public TripUser(String name, String profileImageUrl, Trip trip) {
        this.name = name;
        this.state = TripUserState.UNCONNECTED;
        this.profileImageUrl = profileImageUrl;
        this.trip = trip;
    }

    public String getProfileImageUrl() {
        return (user == null) ? profileImageUrl : user.getProfileImageUrl();
    }

    public String getName() {
        return (user == null) ? name : user.getNickname();
    }

    public Long getUserId() {
        return (user == null) ? null : user.getId();
    }
}
