package oniamey.spotify.oniameyspotifyserver.infrastructure.security.oauth2;

import lombok.RequiredArgsConstructor;
import oniamey.spotify.oniameyspotifyserver.entity.User;
import oniamey.spotify.oniameyspotifyserver.infrastructure.constant.AuthProvider;
import oniamey.spotify.oniameyspotifyserver.infrastructure.constant.Role;
import oniamey.spotify.oniameyspotifyserver.infrastructure.constant.Status;
import oniamey.spotify.oniameyspotifyserver.infrastructure.constant.module.ActorConstants;
import oniamey.spotify.oniameyspotifyserver.infrastructure.security.exception.OAuth2AuthenticationProcessingException;
import oniamey.spotify.oniameyspotifyserver.infrastructure.security.oauth2.user.OAuth2UserInfo;
import oniamey.spotify.oniameyspotifyserver.infrastructure.security.oauth2.user.OAuth2UserInfoFactory;
import oniamey.spotify.oniameyspotifyserver.infrastructure.security.repository.SecurityUserRepository;
import oniamey.spotify.oniameyspotifyserver.infrastructure.security.oauth2.user.UserPrincipal;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final SecurityUserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory
                .getOAuth2UserInfo(
                        oAuth2UserRequest.getClientRegistration().getRegistrationId(),
                        oAuth2User.getAttributes()
                );
        if (oAuth2UserInfo.getEmail() == null || oAuth2UserInfo.getEmail().isBlank()) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<User> userAuthOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());

        if (userAuthOptional.isPresent()) {
            User user = userAuthOptional.get();
            if(user.getStatus().equals(Status.INACTIVE)){
                throw new OAuth2AuthenticationProcessingException("The specified user is disabled");
            }
            User userExist = (User) updateExistingUser(user, oAuth2UserInfo);
            return UserPrincipal.create(userExist, oAuth2User.getAttributes(), userExist.getRole().name());
        }

        Object newUser = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        if (newUser instanceof User originUser) {
            return UserPrincipal.create(originUser, oAuth2User.getAttributes(), ActorConstants.USER);
        } else {
            throw new OAuth2AuthenticationProcessingException("Invalid email format");
        }
    }

    private Object registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        User user = new User();
        user.setUserName(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setSubscriptionType(oAuth2UserInfo.getSubscriptionType());
        user.setProfilePicture(oAuth2UserInfo.getImageUrl());
        user.setStatus(Status.ACTIVE);
        user.setRole(Role.USER);
        user.setPassword(null);
        return userRepository.save(user);
    }

    private Object updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setUserName(oAuth2UserInfo.getName());
        existingUser.setProfilePicture(oAuth2UserInfo.getImageUrl());
        existingUser.setSubscriptionType(oAuth2UserInfo.getSubscriptionType());
        if (existingUser.getStatus() == null) existingUser.setStatus(Status.ACTIVE);
        return userRepository.save(existingUser);
    }

}
