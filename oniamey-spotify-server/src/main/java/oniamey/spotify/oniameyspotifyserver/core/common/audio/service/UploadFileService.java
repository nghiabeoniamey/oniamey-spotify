package oniamey.spotify.oniameyspotifyserver.core.common.audio.service;

import oniamey.spotify.oniameyspotifyserver.core.common.base.ResponseObject;
import org.springframework.web.multipart.MultipartFile;

public interface UploadFileService {

    ResponseObject<?> uploadFile(MultipartFile file);

}
