package oniamey.spotify.oniameyspotifyserver.core.admin.song.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AdminSongRequest {

    private String title;

    private String artist;

    private String releasedDate;

    private List<String> genre;

    private String audioFile;

}
