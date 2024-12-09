package oniamey.spotify.oniameyspotifyserver.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import oniamey.spotify.oniameyspotifyserver.entity.base.PrimaryEntity;
import oniamey.spotify.oniameyspotifyserver.infrastructure.constant.Role;
import oniamey.spotify.oniameyspotifyserver.infrastructure.constant.Status;
import oniamey.spotify.oniameyspotifyserver.infrastructure.constant.module.EntityProperties;

import java.io.Serializable;

@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User extends PrimaryEntity implements Serializable {

    @Column(nullable = false, length = EntityProperties.LENGTH_NAME)
    private String userName;

    @Column(nullable = false, length = EntityProperties.LENGTH_CODE)
    private String email;

    @Column(name = "password_hash", length = EntityProperties.LENGTH_PASSWORD)
    private String password;

    @Column(name = "password_secret", length = EntityProperties.LENGTH_PASSWORD_SECRET)
    private String passwordSecret;

    @Column(length = EntityProperties.LENGTH_CODE)
    private String subscriptionType;

    @Column(length = EntityProperties.LENGTH_URL)
    private String profilePicture;

    @Enumerated(EnumType.ORDINAL)
    private Role role;

    @Enumerated(EnumType.ORDINAL)
    private Status status;

}
