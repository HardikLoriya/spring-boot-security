package com.spring.jwt.refresh.token.web;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import com.spring.jwt.refresh.token.persistance.user.UserEntity;
import com.spring.jwt.refresh.token.security.annotation.Admin;
import com.spring.jwt.refresh.token.service.UserService;
import com.spring.jwt.refresh.token.web.model.ChangePasswordRequest;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    @Admin
    public ResponseEntity<List<UserEntity>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("{username}")
    @Admin
    public ResponseEntity<UserEntity> findById(@PathVariable String username) {
        return ResponseEntity.ok(service.findById(username));
    }

    @PostMapping(
            path = "change-password",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        service.updatePassword(User.withUsername(username)
                .password("[PROTECTED]")
                .authorities(Collections.emptyList())
                .build(), request.password());
        return ResponseEntity.ok("Password was updated");
    }
}
