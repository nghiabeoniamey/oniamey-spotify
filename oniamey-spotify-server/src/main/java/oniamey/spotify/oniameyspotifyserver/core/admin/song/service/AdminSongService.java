package oniamey.spotify.oniameyspotifyserver.core.admin.song.service;

import jakarta.validation.Valid;
import oniamey.spotify.oniameyspotifyserver.core.admin.song.model.request.AdminFindSongRequest;
import oniamey.spotify.oniameyspotifyserver.core.admin.song.model.request.AdminGenreRequest;
import oniamey.spotify.oniameyspotifyserver.core.admin.song.model.request.AdminSongRequest;
import oniamey.spotify.oniameyspotifyserver.core.common.base.ResponseObject;

public interface AdminSongService {

    ResponseObject<?> getSongs(AdminFindSongRequest request);

    ResponseObject<?> getSongById(String id);

    ResponseObject<?> getGenres();

    ResponseObject<?> createSong(@Valid AdminSongRequest request);

    ResponseObject<?> updateSong(String id, @Valid AdminSongRequest request);

    ResponseObject<?> changeStatusSong(String id);

    ResponseObject<?> createGenre(@Valid AdminGenreRequest request);

}
