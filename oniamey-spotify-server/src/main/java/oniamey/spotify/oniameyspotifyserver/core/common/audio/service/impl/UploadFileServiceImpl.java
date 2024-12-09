package oniamey.spotify.oniameyspotifyserver.core.common.audio.service.impl;

import lombok.RequiredArgsConstructor;
import oniamey.spotify.oniameyspotifyserver.core.common.audio.repository.UploadFileAttachmentRepository;
import oniamey.spotify.oniameyspotifyserver.core.common.audio.service.UploadFileService;
import oniamey.spotify.oniameyspotifyserver.core.common.base.ResponseObject;
import oniamey.spotify.oniameyspotifyserver.entity.Attachment;
import oniamey.spotify.oniameyspotifyserver.infrastructure.constant.module.Message;
import oniamey.spotify.oniameyspotifyserver.util.AudioFileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UploadFileServiceImpl implements UploadFileService {

    private static final Logger log = LoggerFactory.getLogger(UploadFileServiceImpl.class);
    private final UploadFileAttachmentRepository attachmentRepository;

    @Override
    public ResponseObject<?> uploadFile(MultipartFile file) {
        log.info("MultipartFile: {}", file.toString());
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            if (fileName.contains("..")) {
                return ResponseObject.errorForward(
                        HttpStatus.BAD_REQUEST,
                        "Tên file không hợp lệ: " + fileName
                );
            }

            Optional<Attachment> attachmentOptional = attachmentRepository.findByFileNameAndFileTypeAndData(
                    fileName,
                    file.getContentType(),
                    file.getBytes()
            );
            if (attachmentOptional.isPresent()) {
                return new ResponseObject<>(
                        attachmentOptional.get(),
                        HttpStatus.OK,
                        Message.Success.UPLOAD_SUCCESS
                );
            }

            Attachment attachment
                    = new Attachment(fileName,
                    file.getContentType(),
                    AudioFileUtil.getDurationFromFile(file),
                    file.getBytes());
            return new ResponseObject<>(
                    attachmentRepository.save(attachment),
                    HttpStatus.OK,
                    Message.Success.UPLOAD_SUCCESS
            );

        } catch (Exception e) {
            return ResponseObject.errorForward(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Không thể tải lên tệp file: " + fileName + " , lỗi: " + e.getMessage()
            );
        }
    }

}
