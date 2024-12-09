package oniamey.spotify.oniameyspotifyserver.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import oniamey.spotify.oniameyspotifyserver.entity.base.PrimaryEntity;
import oniamey.spotify.oniameyspotifyserver.infrastructure.constant.InteractionType;
import oniamey.spotify.oniameyspotifyserver.infrastructure.constant.Status;
import oniamey.spotify.oniameyspotifyserver.infrastructure.constant.module.EntityProperties;

import java.io.Serializable;

@Entity
@Table(name = "playlist")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Playlist extends PrimaryEntity implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "user_id")
    private User user;

    @Column(length = EntityProperties.LENGTH_CONTENT)
    private String title;

    @Column(length = EntityProperties.LENGTH_CONTENT)
    private String description;

    @Column(length = EntityProperties.LENGTH_CODE)
    private String privacySetting;

    @Enumerated(EnumType.ORDINAL)
    private Status status;

    // has creationDate



}
