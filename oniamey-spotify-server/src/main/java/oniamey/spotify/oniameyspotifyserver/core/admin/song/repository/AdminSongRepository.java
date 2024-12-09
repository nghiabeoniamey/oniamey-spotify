package oniamey.spotify.oniameyspotifyserver.core.admin.song.repository;

import oniamey.spotify.oniameyspotifyserver.core.admin.song.model.request.AdminFindSongRequest;
import oniamey.spotify.oniameyspotifyserver.core.admin.song.model.response.AdminDetailSongResponse;
import oniamey.spotify.oniameyspotifyserver.core.admin.song.model.response.AdminSongResponse;
import oniamey.spotify.oniameyspotifyserver.repository.SongRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminSongRepository extends SongRepository {

    @Query(value = """
            SELECT DISTINCT
                ROW_NUMBER() OVER(ORDER BY s.created_date DESC) AS catalog,
                s.title AS title,
                s.id AS id,
                s.artist AS artist,
                s.release_date AS releasedDate,
                s.status AS status,
                (SELECT g.genre) AS genre
            FROM song s
            LEFT JOIN genre_song gs ON gs.song_id = s.id
            LEFT JOIN genre g ON g.id = gs.genre_id
            WHERE
            (:#{#request.keyword} IS NULL OR
                s.title LIKE CONCAT('%', :#{#request.keyword}, '%') OR
                s.artist LIKE CONCAT('%', :#{#request.keyword}, '%'))
            AND :#{#request.genre} IS NULL OR gs.genre_id = :#{#request.genre}
            AND :#{#request.status} IS NULL OR s.status = :#{#request.status}
            """, countQuery = """
            SELECT count(s.created_date)
            FROM song s
            LEFT JOIN genre_song gs ON gs.song_id = s.id
            LEFT JOIN genre g ON g.id = gs.genre_id
            WHERE
            (:#{#request.keyword} IS NULL OR
                s.title LIKE CONCAT('%', :#{#request.keyword}, '%') OR
                s.artist LIKE CONCAT('%', :#{#request.keyword}, '%'))
            AND  gs.genre_id IN :#{#request.genre}
            AND :#{#request.status} IS NULL OR s.status = :#{#request.status}
            """, nativeQuery = true)
    Page<AdminSongResponse> getSongsByGenres(Pageable pageable, AdminFindSongRequest request);

    @Query(value = """
            SELECT DISTINCT
                ROW_NUMBER() OVER(ORDER BY s.created_date DESC) AS catalog,
                s.id AS id,
                s.title AS title,
                s.artist AS artist,
                s.release_date AS releasedDate,
                s.status AS status,
                (SELECT DISTINCT g.genre) AS genre
            FROM song s
            LEFT JOIN genre_song gs ON gs.song_id = s.id
            LEFT JOIN genre g ON g.id = gs.genre_id
            WHERE
            (:#{#request.keyword} IS NULL OR
                s.title LIKE CONCAT('%', :#{#request.keyword}, '%') OR
                s.artist LIKE CONCAT('%', :#{#request.keyword}, '%'))
            AND :#{#request.status} IS NULL OR s.status = :#{#request.status}
            """, countQuery = """
            SELECT count(s.created_date)
            FROM song s
            LEFT JOIN genre_song gs ON gs.song_id = s.id
            LEFT JOIN genre g ON g.id = gs.genre_id
            WHERE
            (:#{#request.keyword} IS NULL OR
                s.title LIKE CONCAT('%', :#{#request.keyword}, '%') OR
                s.artist LIKE CONCAT('%', :#{#request.keyword}, '%'))
            AND :#{#request.status} IS NULL OR s.status = :#{#request.status}
            """, nativeQuery = true)
    Page<AdminSongResponse> getSongs(Pageable pageable, AdminFindSongRequest request);

    @Query(value = """
            SELECT DISTINCT
                s.title AS title,
                s.artist AS artist,
                s.audio_file AS audio_file
            FROM song s
            WHERE s.id = :id
            """, nativeQuery = true)
    AdminDetailSongResponse getDetailSongById(String id);

}
