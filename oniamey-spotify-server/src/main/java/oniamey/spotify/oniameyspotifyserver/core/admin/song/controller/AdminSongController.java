package oniamey.spotify.oniameyspotifyserver.core.admin.song.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import oniamey.spotify.oniameyspotifyserver.core.admin.song.model.request.AdminFindSongRequest;
import oniamey.spotify.oniameyspotifyserver.core.admin.song.model.request.AdminGenreRequest;
import oniamey.spotify.oniameyspotifyserver.core.admin.song.model.request.AdminSongRequest;
import oniamey.spotify.oniameyspotifyserver.core.admin.song.service.AdminSongService;
import oniamey.spotify.oniameyspotifyserver.infrastructure.constant.module.MappingConstant;
import oniamey.spotify.oniameyspotifyserver.util.Helper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(MappingConstant.API_ADMIN_SONG)
@RestController
@RequiredArgsConstructor
public class AdminSongController {

    private final AdminSongService songService;

    @GetMapping()
    public ResponseEntity<?> getSongs(@Valid final AdminFindSongRequest request) {
        return Helper.createResponseEntity(songService.getSongs(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSong(@PathVariable String id) {
        return Helper.createResponseEntity(songService.getSongById(id));
    }

    @GetMapping("/genre")
    public ResponseEntity<?> getGenres() {
        return Helper.createResponseEntity(songService.getGenres());
    }

    @PostMapping()
    public ResponseEntity<?> createSong(@Valid @RequestBody final AdminSongRequest request) {
        return Helper.createResponseEntity(songService.createSong(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSong(@PathVariable String id, @Valid @RequestBody final AdminSongRequest request) {
        return Helper.createResponseEntity(songService.updateSong(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> changeStatusSong(@PathVariable String id) {
        return Helper.createResponseEntity(songService.changeStatusSong(id));
    }

    @PostMapping("/genre")
    public ResponseEntity<?> createGenre(@Valid @RequestBody final AdminGenreRequest request) {
        return Helper.createResponseEntity(songService.createGenre(request));
    }

}
