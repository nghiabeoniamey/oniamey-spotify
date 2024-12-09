package oniamey.spotify.oniameyspotifyserver.core.admin.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oniamey.spotify.oniameyspotifyserver.core.admin.user.model.request.AdminFindUserRequest;
import oniamey.spotify.oniameyspotifyserver.core.admin.user.model.request.AdminUserRequest;
import oniamey.spotify.oniameyspotifyserver.core.admin.user.service.AdminUserService;
import oniamey.spotify.oniameyspotifyserver.infrastructure.constant.module.MappingConstant;
import oniamey.spotify.oniameyspotifyserver.util.Helper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(MappingConstant.API_ADMIN_USER)
@RequiredArgsConstructor
@RestController
@Slf4j
public class AdminUserController {

    private final AdminUserService userService;

    @GetMapping()
    public ResponseEntity<?> getUsers(@Valid final AdminFindUserRequest request) {
        return Helper.createResponseEntity(userService.getUsers(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable String id) {
        return Helper.createResponseEntity(userService.getUserById(id));
    }

    @PostMapping()
    public ResponseEntity<?> createUser(@Valid final AdminUserRequest request) {
        return Helper.createResponseEntity(userService.createUser(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @Valid final AdminUserRequest request) {
        return Helper.createResponseEntity(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> changeStatusUser(@PathVariable String id) {
        return Helper.createResponseEntity(userService.changeStatusUser(id));
    }

}
