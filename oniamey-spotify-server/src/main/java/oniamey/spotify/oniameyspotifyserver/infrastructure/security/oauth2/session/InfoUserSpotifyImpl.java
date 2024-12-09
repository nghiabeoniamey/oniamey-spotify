package oniamey.spotify.oniameyspotifyserver.infrastructure.security.oauth2.session;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import oniamey.spotify.oniameyspotifyserver.infrastructure.constant.auth.Session;
import oniamey.spotify.oniameyspotifyserver.infrastructure.security.model.response.InfoUserSpotifyResponse;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InfoUserSpotifyImpl implements InfoUserSpotify {

    private final HttpSession httpSession;

    @Override
    public String getId() {
        return httpSession.getAttribute(Session.CURRENT_USER_ID).toString();
    }

    @Override
    public String getUserName() {
        return httpSession.getAttribute(Session.CURRENT_USER_NAME).toString();
    }

    @Override
    public String getEmail() {
        return httpSession.getAttribute(Session.CURRENT_USER_EMAIL).toString();
    }

    @Override
    public String getSubscriptionType() {
        return httpSession.getAttribute(Session.CURRENT_USER_SUBSCRIPTION_TYPE).toString();
    }

    @Override
    public String getProfilePicture() {
        return httpSession.getAttribute(Session.CURRENT_USER_PROFILE_PICTURE).toString();
    }

    @Override
    public String getRoleCode() {
        return httpSession.getAttribute(Session.CURRENT_USER_ROLE_CODE).toString();
    }

    @Override
    public String getRoleName() {
        return httpSession.getAttribute(Session.CURRENT_USER_ROLE_NAME).toString();
    }

    @Override
    public String getHost() {
        return httpSession.getAttribute(Session.CURRENT_HOST).toString();
    }

    @Override
    public InfoUserSpotifyResponse getInfoUserSpotify() {
        return new InfoUserSpotifyResponse(
                getId(),
                getUserName(),
                getEmail(),
                getSubscriptionType(),
                getProfilePicture(),
                getRoleCode(),
                getRoleName(),
                getHost()
        );
    }
}
