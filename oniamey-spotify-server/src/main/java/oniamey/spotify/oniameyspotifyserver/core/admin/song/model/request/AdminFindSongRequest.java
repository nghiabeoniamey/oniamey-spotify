package oniamey.spotify.oniameyspotifyserver.core.admin.song.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import oniamey.spotify.oniameyspotifyserver.core.common.base.PageableRequest;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdminFindSongRequest extends PageableRequest {

    private String keyword;

    private List<Integer> genre;

    private Integer status;
}
