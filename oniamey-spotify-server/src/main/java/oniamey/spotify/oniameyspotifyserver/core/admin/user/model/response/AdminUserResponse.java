package oniamey.spotify.oniameyspotifyserver.core.admin.user.model.response;

import oniamey.spotify.oniameyspotifyserver.core.common.base.BaseResponse;

public interface AdminUserResponse extends BaseResponse {

    String getName();

    String getEmail();

    String getPassword();

    String getSubscriptionType();

    String getProfilePicture();

    Integer getRole();

    Integer getStatus();

}
