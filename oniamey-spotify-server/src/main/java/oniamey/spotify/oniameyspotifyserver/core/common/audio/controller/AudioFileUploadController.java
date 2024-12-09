package oniamey.spotify.oniameyspotifyserver.core.common.audio.controller;

import lombok.RequiredArgsConstructor;
import oniamey.spotify.oniameyspotifyserver.core.common.audio.service.UploadFileService;
import oniamey.spotify.oniameyspotifyserver.infrastructure.constant.module.MappingConstant;
import oniamey.spotify.oniameyspotifyserver.util.Helper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(MappingConstant.API_COMMON_UPLOAD)
@RequiredArgsConstructor
public class AudioFileUploadController {

    private final UploadFileService uploadFileService;

    @PostMapping()
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        return Helper.createResponseEntity(uploadFileService.uploadFile(file));
    }

}
