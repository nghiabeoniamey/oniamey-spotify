package oniamey.spotify.oniameyspotifyserver.repository;

import oniamey.spotify.oniameyspotifyserver.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
}