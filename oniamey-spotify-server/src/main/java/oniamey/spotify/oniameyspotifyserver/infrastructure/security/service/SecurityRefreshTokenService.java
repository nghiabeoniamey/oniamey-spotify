package oniamey.spotify.oniameyspotifyserver.infrastructure.security.service;

import jakarta.validation.Valid;
import oniamey.spotify.oniameyspotifyserver.core.common.base.ResponseObject;
import oniamey.spotify.oniameyspotifyserver.infrastructure.security.model.request.AuthLoginRequest;
import oniamey.spotify.oniameyspotifyserver.infrastructure.security.model.request.AuthRefreshRequest;
import oniamey.spotify.oniameyspotifyserver.infrastructure.security.model.request.AuthRegisterRequest;

public interface SecurityRefreshTokenService {

    ResponseObject<?> getRefreshToken(@Valid AuthRefreshRequest request);

    ResponseObject<?> logout(@Valid AuthRefreshRequest request);

    ResponseObject<?> login(@Valid AuthLoginRequest request);

    ResponseObject<?> register(@Valid AuthRegisterRequest request);

}
