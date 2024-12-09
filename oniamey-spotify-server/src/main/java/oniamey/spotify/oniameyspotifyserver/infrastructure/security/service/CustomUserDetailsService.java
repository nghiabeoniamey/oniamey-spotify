package oniamey.spotify.oniameyspotifyserver.infrastructure.security.service;

import lombok.RequiredArgsConstructor;
import oniamey.spotify.oniameyspotifyserver.entity.User;
import oniamey.spotify.oniameyspotifyserver.infrastructure.security.repository.SecurityUserRepository;
import oniamey.spotify.oniameyspotifyserver.infrastructure.security.oauth2.user.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final SecurityUserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String role = user.getRole().name();
            return UserPrincipal.create(user, role);
        }

        throw new UsernameNotFoundException("user not found with email : " + email);
    }

}