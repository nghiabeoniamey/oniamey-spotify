package oniamey.spotify.oniameyspotifyserver.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import oniamey.spotify.oniameyspotifyserver.entity.base.PrimaryEntity;
import oniamey.spotify.oniameyspotifyserver.infrastructure.constant.Status;
import oniamey.spotify.oniameyspotifyserver.infrastructure.constant.module.EntityProperties;

import java.io.Serializable;

@Entity
@Table(name = "song")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Song extends PrimaryEntity implements Serializable {

    @Column(length = EntityProperties.LENGTH_NAME)
    private String title;

    @Column(length = EntityProperties.LENGTH_NAME)
    private String artist;

    private Long releaseDate;

    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "attachment_id")
    private Attachment attachment;
}
