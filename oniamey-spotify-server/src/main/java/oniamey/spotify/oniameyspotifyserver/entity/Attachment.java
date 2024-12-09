package oniamey.spotify.oniameyspotifyserver.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import oniamey.spotify.oniameyspotifyserver.entity.base.PrimaryEntity;

import java.io.Serializable;

@Entity
@Table(name = "attachment")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Attachment extends PrimaryEntity implements Serializable {

    private String fileName;
    private String fileType;
    private Long duration;

    @Column(updatable = false)
    @Lob
    private byte[] data;

}
