package com.spring.jwt.refresh.token;


import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.spring.jwt.refresh.token.persistance.user.UserEntity;
import com.spring.jwt.refresh.token.persistance.user.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BasicControllerTest {

    @Autowired
    private WebTestClient http;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository repository;

    @BeforeEach
    void populate() {
        var u = new UserEntity();
        u.setUsername("hardik");
        u.setPassword(passwordEncoder.encode("password"));
        u.setAuthorities(List.of("ROLE_ADMIN", "ROLE_USER"));
        u.setEnabled(Boolean.TRUE);
        repository.save(u);
    }

    @AfterEach
    void truncate() {
        repository.deleteAll();
    }

    @Test
    void me() {
        this.http
                .get()
                .uri("/api/v1/basic/me")
                .headers(headers -> headers.setBasicAuth("hardik", "password"))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(UserEntity.class)
                .value(UserEntity::getUsername, Matchers.equalTo("hardik"))
                .value(UserEntity::getAuthorities, Matchers.hasSize(2))
                .value(UserEntity::isEnabled, Matchers.equalTo(true));

    }

    @Test
    void meWrongPassword() {
        this.http
                .get()
                .uri("/api/v1/basic/me")
                .headers(headers -> headers.setBasicAuth("hardik", "wrong-password"))
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }
}