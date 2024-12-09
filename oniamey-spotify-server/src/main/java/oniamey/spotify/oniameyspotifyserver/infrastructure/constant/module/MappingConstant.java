package oniamey.spotify.oniameyspotifyserver.infrastructure.constant.module;

public class MappingConstant {

    public static final String USER = "/user";
    public static final String ADMIN = "/admin";

    public static final String EMBED = "/embed";

    public static final String API_VERSION_PREFIX = "/api/v1";
    public static final String API_COMMON = API_VERSION_PREFIX + "/common";

    public static final String API_GUEST_GUARD_ACCOUNT = "/api/guest-guard/account";

    public static final String API_USER_PREFIX = API_VERSION_PREFIX + USER;
    public static final String API_ADMIN_PREFIX = API_VERSION_PREFIX + ADMIN;

    public static final String API_EMBED_PREFIX = API_VERSION_PREFIX + EMBED;

    public static final String API_USER_SONG = API_USER_PREFIX + "/song";
    public static final String API_USER_PLAYLIST = API_USER_PREFIX + "/playlist";
    public static final String API_USER_INJECTION = API_USER_PREFIX + "/injection";
    public static final String API_USER_FOLLOW = API_USER_PREFIX + "/follow";
    public static final String API_USER_ACCOUNT = API_USER_PREFIX + "/account";
    public static final String API_USER_HISTORY = API_USER_PREFIX + "/history";

    public static final String API_ADMIN_USER = API_ADMIN_PREFIX + "/user";
    public static final String API_ADMIN_SONG = API_ADMIN_PREFIX + "/song";

    public static final String API_COMMON_UPLOAD = API_COMMON + "/upload";

    /* AUTHENTICATION */
    public static final String API_AUTH_PREFIX = API_VERSION_PREFIX + "/auth";

    public static final String PATH_OAUTH2 = "/oauth2";

}
