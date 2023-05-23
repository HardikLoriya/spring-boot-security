package com.spring.jwt.refresh.token.controller;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.jwt.refresh.token.model.ChangePasswordRequest;
import com.spring.jwt.refresh.token.persistance.user.UserEntity;
import com.spring.jwt.refresh.token.security.annotation.Admin;
import com.spring.jwt.refresh.token.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
@Validated
public class UserController {

    private final UserService service;

    @GetMapping
    @Admin
    public ResponseEntity<List<UserEntity>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("{username}")
    @Admin
    public ResponseEntity<UserEntity> findById(@PathVariable @NotEmpty(message = "username can't be empty") String username) {
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
