package oniamey.spotify.oniameyspotifyserver.infrastructure.config.database;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import oniamey.spotify.oniameyspotifyserver.infrastructure.config.database.service.DBGenAttachmentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DBGenerator {

    private final DBGenAttachmentService dbGenAttachmentService;

    @Value("${db.generator.is-generated}")
    private String isGenerated;

    @PostConstruct
    public void init() {
        if ("true".equals(isGenerated)) generateData();
    }

    private void generateData() {
        dbGenAttachmentService.updateDataColumn();
    }

}
