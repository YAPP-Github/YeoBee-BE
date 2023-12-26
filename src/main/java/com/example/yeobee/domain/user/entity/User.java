package com.example.yeobee.domain.user.entity;

import com.example.yeobee.common.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AuthProvider> authProviderList = new ArrayList<>();

    public void addAuthProvider(AuthProvider authProvider) {
        authProviderList.add(authProvider);
        if (authProvider.getUser() == null) {
            authProvider.setUser(this);
        }
    }
}
