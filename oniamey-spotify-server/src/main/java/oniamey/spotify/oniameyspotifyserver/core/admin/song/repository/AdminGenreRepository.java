package oniamey.spotify.oniameyspotifyserver.core.admin.song.repository;

import oniamey.spotify.oniameyspotifyserver.core.admin.song.model.response.AdminGenreResponse;
import oniamey.spotify.oniameyspotifyserver.repository.GenreRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminGenreRepository extends GenreRepository {

    @Query(value = """
                SELECT g.id, g.genre AS name, g.description  FROM genre g
            """, nativeQuery = true)
    List<AdminGenreResponse> getGenres();

    boolean existsByGenre(String genre);

}
