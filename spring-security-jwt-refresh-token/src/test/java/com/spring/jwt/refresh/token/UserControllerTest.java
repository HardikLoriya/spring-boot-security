package com.spring.jwt.refresh.token;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.spring.jwt.refresh.token.persistance.user.UserEntity;
import com.spring.jwt.refresh.token.persistance.user.UserRepository;
import com.spring.jwt.refresh.token.security.TokenProvider;
import com.spring.jwt.refresh.token.web.model.ChangePasswordRequest;
import com.spring.jwt.refresh.token.web.model.TokenResponse;
import com.spring.jwt.refresh.token.web.model.UsernameAndPasswordRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    private WebTestClient http;
    @Autowired
    private TokenProvider provider;
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
    void findAll() {
        var testToken = provider.generate("test", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        http.get()
                .uri("/api/v1/user")
                .headers(headers -> headers.setBearerAuth(testToken))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(UserEntity.class)
                .hasSize(1);
    }

    @Test
    void findAllForbidden() {
        var testToken = provider.generate("test", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        http.get()
                .uri("/api/v1/user")
                .headers(headers -> headers.setBearerAuth(testToken))
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    void findById() {
        var testToken = provider.generate("test", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        http.get()
                .uri("/api/v1/user/hardik")
                .headers(headers -> headers.setBearerAuth(testToken))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(UserEntity.class)
                .value(UserEntity::getUsername, Matchers.equalTo("hardik"))
                .value(UserEntity::getAuthorities, Matchers.hasSize(2))
                .value(UserEntity::isEnabled, Matchers.equalTo(true));
    }

    @Test
    void changePassword() {
        var jwt = http.post()
                .uri("/api/v1/jwt/token")
                .bodyValue(new UsernameAndPasswordRequest("hardik", "password"))
                .exchange()
                .returnResult(TokenResponse.class)
                .getResponseBody()
                .blockLast()
                .jwt();

        http.post()
                .uri("/api/v1/user/change-password")
                .headers(headers -> headers.setBearerAuth(jwt))
                .bodyValue(new ChangePasswordRequest("newPassword"))
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        http.post()
                .uri("/api/v1/jwt/token")
                .bodyValue(new UsernameAndPasswordRequest("hardik", "newPassword"))
                .exchange()
                .expectStatus()
                .is2xxSuccessful();
    }
}