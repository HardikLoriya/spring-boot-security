package com.spring.jwt.refresh.token.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.jwt.refresh.token.persistance.user.UserEntity;
import com.spring.jwt.refresh.token.persistance.user.UserRepository;
import com.spring.jwt.refresh.token.security.UserPrincipal;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService, UserDetailsPasswordService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserPrincipal loadUserByUsername(String username) {
        return repository.findById(username)
                .map(UserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        var entity = repository.findById(user.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        entity.setPassword(passwordEncoder.encode(newPassword));
        return new UserPrincipal(repository.save(entity));
    }

    public List<UserEntity> findAll() {
        return repository.findAll();
    }

    public UserEntity findById(String username) {
        return repository.findById(username).orElseThrow();
    }
}
