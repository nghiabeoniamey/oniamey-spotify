package oniamey.spotify.oniameyspotifyserver.core.admin.song.model.response;

import oniamey.spotify.oniameyspotifyserver.core.common.base.BaseResponse;

public interface AdminSongResponse extends BaseResponse {

    String getTitle();

    String getArtist();

    Long getReleasedDate();

    Integer getStatus();

}