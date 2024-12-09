package oniamey.spotify.oniameyspotifyserver.core.admin.user.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import oniamey.spotify.oniameyspotifyserver.core.common.base.PageableRequest;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdminFindUserRequest extends PageableRequest {

    private String keyword;

    private Integer status;

    private Integer role;

    private String subscriptionType;

}
