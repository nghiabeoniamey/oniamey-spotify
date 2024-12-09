package oniamey.spotify.oniameyspotifyserver.infrastructure.constant;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum FileType {

    MP4,
    MP3,
    WAV,
    FLAC,
    AAC,
    OGG;

    public static List<String> FileTypes() {
        return Arrays.stream(FileType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

}
