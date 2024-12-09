package oniamey.spotify.oniameyspotifyserver.infrastructure.config.database.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class DBGenAttachmentService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void updateDataColumn() {
        entityManager.createNativeQuery("ALTER TABLE attachment MODIFY COLUMN data LONGBLOB")
                .executeUpdate();
    }

}
