package oniamey.spotify.oniameyspotifyserver.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import oniamey.spotify.oniameyspotifyserver.entity.base.PrimaryEntity;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "refresh_token")
public class RefreshToken extends PrimaryEntity implements Serializable {

    @Column(name = "refresh_token", length = 8000)
    private String refreshToken;

    @Column(name = "expired_at")
    private Long expiredAt;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "revoked_at")
    private Long revokedAt;

}
