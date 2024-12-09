package oniamey.spotify.oniameyspotifyserver.core.common.audio.repository;

import oniamey.spotify.oniameyspotifyserver.entity.Attachment;
import oniamey.spotify.oniameyspotifyserver.repository.AttachmentRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UploadFileAttachmentRepository extends AttachmentRepository {

    Optional<Attachment> findByFileNameAndFileTypeAndData(String fileName, String fileType, byte[] data);

}
