package com.spring.security.jwt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MyUserDetailsService implements UserDetailsService {
	
	@Value("${spring.security.user.name}")
	String userName;
	
	@Value("${spring.security.user.password}")
	String password;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return new User(userName, passwordEndcoder().encode(password),
                new ArrayList<>());
    }
    
    @Bean
    PasswordEncoder passwordEndcoder() {
    	return new BCryptPasswordEncoder();
    }

}