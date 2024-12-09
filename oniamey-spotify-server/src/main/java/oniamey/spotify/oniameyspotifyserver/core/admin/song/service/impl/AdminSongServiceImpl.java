package oniamey.spotify.oniameyspotifyserver.core.admin.song.service.impl;

import lombok.RequiredArgsConstructor;
import oniamey.spotify.oniameyspotifyserver.core.admin.song.model.request.AdminFindSongRequest;
import oniamey.spotify.oniameyspotifyserver.core.admin.song.model.request.AdminGenreRequest;
import oniamey.spotify.oniameyspotifyserver.core.admin.song.model.request.AdminSongRequest;
import oniamey.spotify.oniameyspotifyserver.core.admin.song.repository.AdminAttachmentRepository;
import oniamey.spotify.oniameyspotifyserver.core.admin.song.repository.AdminGenreRepository;
import oniamey.spotify.oniameyspotifyserver.core.admin.song.repository.AdminGenreSongRepository;
import oniamey.spotify.oniameyspotifyserver.core.admin.song.repository.AdminSongRepository;
import oniamey.spotify.oniameyspotifyserver.core.admin.song.service.AdminSongService;
import oniamey.spotify.oniameyspotifyserver.core.common.base.PageableObject;
import oniamey.spotify.oniameyspotifyserver.core.common.base.ResponseObject;
import oniamey.spotify.oniameyspotifyserver.entity.Attachment;
import oniamey.spotify.oniameyspotifyserver.entity.Genre;
import oniamey.spotify.oniameyspotifyserver.entity.GenreSong;
import oniamey.spotify.oniameyspotifyserver.entity.Song;
import oniamey.spotify.oniameyspotifyserver.infrastructure.constant.Status;
import oniamey.spotify.oniameyspotifyserver.infrastructure.constant.module.Message;
import oniamey.spotify.oniameyspotifyserver.util.DateTimeUtil;
import oniamey.spotify.oniameyspotifyserver.util.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminSongServiceImpl implements AdminSongService {

    private static final Logger log = LoggerFactory.getLogger(AdminSongServiceImpl.class);

    private final AdminSongRepository songRepository;

    private final AdminGenreRepository genreRepository;

    private final AdminGenreSongRepository genreSongRepository;

    private final AdminAttachmentRepository attachmentRepository;

    @Override
    public ResponseObject<?> getSongs(AdminFindSongRequest request) {
        try {
            Pageable pageable = Helper.createPageable(request);
            if (request.getGenre() == null || request.getGenre().isEmpty()) {
                return new ResponseObject<>(
                        PageableObject.of(songRepository.getSongs(pageable, request)),
                        HttpStatus.OK,
                        Message.Success.GET_SUCCESS
                );
            }
            return new ResponseObject<>(
                    PageableObject.of(songRepository.getSongsByGenres(pageable, request)),
                    HttpStatus.OK,
                    Message.Success.GET_SUCCESS
            );
        } catch (Exception e) {
            return ResponseObject.errorForward(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage()
            );
        }
    }

    @Override
    public ResponseObject<?> getSongById(String id) {
        try {
            return new ResponseObject<>(
                    songRepository.getDetailSongById(id),
                    HttpStatus.OK,
                    Message.Success.GET_SUCCESS
            );
        } catch (Exception e) {
            return ResponseObject.errorForward(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage()
            );
        }
    }

    @Override
    public ResponseObject<?> getGenres() {
        try {
            return new ResponseObject<>(
                    genreRepository.getGenres(),
                    HttpStatus.OK,
                    Message.Success.GET_SUCCESS
            );
        } catch (Exception e) {
            return ResponseObject.errorForward(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage()
            );
        }
    }

    @Override
    public ResponseObject<?> createSong(AdminSongRequest request) {
        try {
            return getResponseObjectSong(request, new Song());
        } catch (Exception e) {
            return ResponseObject.errorForward(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage()
            );
        }
    }

    @Override
    public ResponseObject<?> updateSong(String id, AdminSongRequest request) {
        try {
            Optional<Song> songOptional = songRepository.findById(id);
            if (songOptional.isEmpty()) {
                return ResponseObject.errorForward(
                        HttpStatus.BAD_REQUEST,
                        Message.Response.NOT_FOUND + ", song"
                );
            }
            return getResponseObjectSong(request, songOptional.get());
        } catch (Exception e) {
            return ResponseObject.errorForward(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage()
            );
        }
    }

    private ResponseObject<?> getResponseObjectSong(AdminSongRequest request, Song newSong) throws IOException {
        try {
            Optional<Attachment> attachmentOptional = attachmentRepository.findById(request.getAudioFile());
            if (attachmentOptional.isEmpty()) {
                return ResponseObject.errorForward(
                        HttpStatus.BAD_REQUEST,
                        Message.Response.NOT_FOUND + ", attachment - tệp âm thanh không rõ"
                );
            }
            Attachment attachment = attachmentOptional.get();
            newSong.setTitle(request.getTitle());
            newSong.setArtist(request.getArtist());
            newSong.setReleaseDate(DateTimeUtil.convertStringToTimeStampSecond(request.getReleasedDate()));
            newSong.setStatus(Status.ACTIVE);
            newSong.setAttachment(attachment);
            Song song = songRepository.save(newSong);
            for (Genre genre : genreRepository.findAllById(request.getGenre())) {
                GenreSong genreSong = new GenreSong();
                genreSong.setGenre(genre);
                genreSong.setSong(song);
                genreSongRepository.save(genreSong);
            }
            return ResponseObject.successForward(
                    HttpStatus.CREATED,
                    Message.Success.CREATE_SUCCESS
            );
        } catch (Exception e) {
            return ResponseObject.errorForward(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage()
            );
        }
    }

    @Override
    public ResponseObject<?> changeStatusSong(String id) {
        try {
            Optional<Song> songOptional = songRepository.findById(id);
            if (songOptional.isEmpty()) {
                return ResponseObject.errorForward(
                        HttpStatus.BAD_REQUEST,
                        Message.Response.NOT_FOUND + ", song"
                );
            }
            Song newSong = songOptional.get();
            Status status = newSong.getStatus() == Status.ACTIVE ? Status.INACTIVE : Status.ACTIVE;
            newSong.setStatus(status);
            songRepository.save(newSong);
            return ResponseObject.successForward(
                    HttpStatus.CREATED,
                    Message.Success.UPDATE_SUCCESS
            );
        } catch (Exception e) {
            return ResponseObject.errorForward(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage()
            );
        }
    }

    @Override
    public ResponseObject<?> createGenre(AdminGenreRequest request) {
        try {
            if (genreRepository.existsByGenre(request.getGenre())) {
                return ResponseObject.errorForward(
                        HttpStatus.BAD_REQUEST,
                        Message.Response.DUPLICATE + ", genre"
                );
            }
            Genre genre = new Genre();
            genre.setGenre(request.getGenre());
            genreRepository.save(genre);
            return ResponseObject.successForward(
                    HttpStatus.CREATED,
                    Message.Success.CREATE_SUCCESS
            );
        } catch (Exception e) {
            return ResponseObject.errorForward(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage()
            );
        }
    }
}
