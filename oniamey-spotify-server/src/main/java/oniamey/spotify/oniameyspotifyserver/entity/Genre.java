package oniamey.spotify.oniameyspotifyserver.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import oniamey.spotify.oniameyspotifyserver.entity.base.PrimaryEntity;
import oniamey.spotify.oniameyspotifyserver.infrastructure.constant.module.EntityProperties;

import java.io.Serializable;

@Entity
@Table(name = "genre")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Genre extends PrimaryEntity implements Serializable {

    @Column(length = EntityProperties.LENGTH_NAME)
    private String genre;

    @Column(length = EntityProperties.LENGTH_DESCRIPTION)
    private String description;

}
