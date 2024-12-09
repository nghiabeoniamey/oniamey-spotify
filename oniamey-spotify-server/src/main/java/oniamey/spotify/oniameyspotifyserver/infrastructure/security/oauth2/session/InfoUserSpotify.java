package oniamey.spotify.oniameyspotifyserver.infrastructure.security.oauth2.session;

import oniamey.spotify.oniameyspotifyserver.infrastructure.security.model.response.InfoUserSpotifyResponse;

public interface InfoUserSpotify {

    String getId();

    String getUserName();

    String getEmail();

    String getSubscriptionType();

    String getProfilePicture();

    String getRoleCode();

    String getRoleName();

    String getHost();

    InfoUserSpotifyResponse getInfoUserSpotify();

}
