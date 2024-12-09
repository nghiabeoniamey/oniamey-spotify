package oniamey.spotify.oniameyspotifyserver.infrastructure.config.database.repository;

import oniamey.spotify.oniameyspotifyserver.repository.AttachmentRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DBGenAttachmentRepository extends AttachmentRepository {

    @Query(value = """
        ALTER TABLE attachment MODIFY COLUMN data LONGBLOB;
    """, nativeQuery = true)
    void updateDataColumns();

}
