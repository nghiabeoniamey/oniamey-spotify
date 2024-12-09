package oniamey.spotify.oniameyspotifyserver.util;

import oniamey.spotify.oniameyspotifyserver.entity.Attachment;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class AudioFileUtil {

    private static final Logger log = LoggerFactory.getLogger(AudioFileUtil.class);

    public static Long getDurationFromFile(MultipartFile file) {
        try {
            Tika tika = new Tika();
            Metadata metadata = new Metadata();
            tika.parse(file.getInputStream(), metadata);
            log.info("tika + {}", tika.parse(file.getInputStream()).toString());
            log.info("metadata + {}", metadata);
            String durationString = metadata.get("xmpDM:duration");
            if (durationString != null) {
                return Math.round(Double.parseDouble(durationString));
            }
        } catch (Exception e) {
            log.info("getDurationFromFile + {}", e.getMessage());
            e.printStackTrace(System.out);
        }
        log.info("getDurationFromFile is return null");
        return null;
    }

}
