package oniamey.spotify.oniameyspotifyserver.infrastructure.security.service.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oniamey.spotify.oniameyspotifyserver.core.common.base.ResponseObject;
import oniamey.spotify.oniameyspotifyserver.entity.RefreshToken;
import oniamey.spotify.oniameyspotifyserver.entity.User;
import oniamey.spotify.oniameyspotifyserver.infrastructure.constant.Role;
import oniamey.spotify.oniameyspotifyserver.infrastructure.constant.Status;
import oniamey.spotify.oniameyspotifyserver.infrastructure.security.model.request.AuthLoginRequest;
import oniamey.spotify.oniameyspotifyserver.infrastructure.security.model.request.AuthRefreshRequest;
import oniamey.spotify.oniameyspotifyserver.infrastructure.security.model.request.AuthRegisterRequest;
import oniamey.spotify.oniameyspotifyserver.infrastructure.security.model.response.AuthRefreshResponse;
import oniamey.spotify.oniameyspotifyserver.infrastructure.security.model.response.TokenUriResponse;
import oniamey.spotify.oniameyspotifyserver.infrastructure.security.repository.SecurityRefreshRepository;
import oniamey.spotify.oniameyspotifyserver.infrastructure.security.repository.SecurityUserRepository;
import oniamey.spotify.oniameyspotifyserver.infrastructure.security.service.SecurityRefreshTokenService;
import oniamey.spotify.oniameyspotifyserver.infrastructure.security.service.RefreshTokenService;
import oniamey.spotify.oniameyspotifyserver.infrastructure.security.service.TokenProvider;
import oniamey.spotify.oniameyspotifyserver.util.AESPasswordCryptoUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.crypto.SecretKey;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class SecurityRefreshTokenServiceImpl implements SecurityRefreshTokenService {

    private final TokenProvider tokenProvider;

    private final SecurityRefreshRepository authRefreshTokenRepository;

    private final SecurityUserRepository authUserRepository;

    private final RefreshTokenService refreshTokenService;

    @Override
    public ResponseObject<?> getRefreshToken(@Valid AuthRefreshRequest request) {
        try {
            String refreshToken = request.getRefreshToken();

            Optional<RefreshToken> refreshTokenOptional = authRefreshTokenRepository.findByRefreshToken(refreshToken);
            if (refreshTokenOptional.isEmpty()) {
                return ResponseObject.errorForward(HttpStatus.NOT_FOUND, "Refresh token not found");
            }

            RefreshToken refreshTokenEntity = refreshTokenOptional.get();
            if (refreshTokenEntity.getRevokedAt() != null) {
                return ResponseObject.errorForward(HttpStatus.BAD_REQUEST, "Refresh token has been revoked");
            }

            String accessToken = tokenProvider.createToken(refreshTokenEntity.getUserId());
            return ResponseObject.successForward(new AuthRefreshResponse(accessToken, refreshToken), "Get refresh token successfully");
        } catch (Exception e) {
            return ResponseObject.errorForward(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @Override
    public ResponseObject<?> logout(@Valid AuthRefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        Optional<RefreshToken> refreshTokenOptional = authRefreshTokenRepository.findByRefreshToken(refreshToken);
        if (refreshTokenOptional.isEmpty()) {
            return ResponseObject.errorForward(HttpStatus.NOT_FOUND, "Refresh token not found");
        }

        RefreshToken refreshTokenEntity = refreshTokenOptional.get();
        refreshTokenEntity.setRevokedAt(System.currentTimeMillis());
        authRefreshTokenRepository.save(refreshTokenEntity);

        return ResponseObject.successForward(null, "Logout successfully");
    }

    @Override
    public ResponseObject<?> login(AuthLoginRequest request) {
        try {
            Optional<User> userOptional = authUserRepository.findByEmail(request.getEmail());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                String passwordSecret = user.getPasswordSecret();
                SecretKey restoredKey = AESPasswordCryptoUtil.decodeKeyFromString(passwordSecret);
                String decryptedPassword = AESPasswordCryptoUtil.decrypt(user.getPassword(), restoredKey);
                if (decryptedPassword.matches(request.getPassword())) {
                    String accessToken = tokenProvider.createToken(user.getId());
                    String refreshToken = refreshTokenService.createRefreshToken(user.getId()).getRefreshToken();
                    return ResponseObject.successForward(TokenUriResponse.getState(accessToken, refreshToken), "Get state successfully");
                } else {
                    return ResponseObject.errorForward(HttpStatus.BAD_REQUEST, "Incorrect password");
                }
            }
            return ResponseObject.errorForward(HttpStatus.BAD_REQUEST, "User does not exits");
        } catch (Exception e) {
            log.info("😢😢 ~> Error login");
            return ResponseObject.errorForward(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public ResponseObject<?> register(AuthRegisterRequest request) {
        try {
            Optional<User> userOptional = authUserRepository.findByEmail(request.getEmail());
            if (userOptional.isPresent()) {
                return ResponseObject.errorForward(HttpStatus.BAD_REQUEST, "Email already in use");
            }
            User user = new User();
            user.setEmail(request.getEmail());
            SecretKey secretKey = AESPasswordCryptoUtil.generateSecretKey();
            String encodedPassword = AESPasswordCryptoUtil.encrypt(request.getPassword(), secretKey);
            String encodeSecretKey = AESPasswordCryptoUtil.encodeKeyToString(secretKey);
            user.setPassword(encodedPassword);
            user.setPasswordSecret(encodeSecretKey);
            user.setRole(Role.USER);
            user.setStatus(Status.ACTIVE);
            String userId = authUserRepository.save(user).getId();
            String accessToken = tokenProvider.createToken(userId);
            String refreshToken = refreshTokenService.createRefreshToken(userId).getRefreshToken();
            return ResponseObject.successForward(TokenUriResponse.getState(accessToken, refreshToken), "Get state successfully");

        } catch (Exception e) {
            log.info("😢😢 ~> Error encrypt register");
            return ResponseObject.errorForward(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
