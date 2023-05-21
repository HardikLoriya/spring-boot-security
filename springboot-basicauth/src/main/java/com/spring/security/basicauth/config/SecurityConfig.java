package com.spring.security.basicauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Value("${spring.security.user.name}")
	String userName;
	
	@Value("${spring.security.user.password}")
	String password;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception 
    {
        http
         .csrf().disable()
         .authorizeRequests().anyRequest().authenticated()
         .and()
         .httpBasic()
         .and()
         .logout()                                                                
         .logoutUrl("/logout")                                                 
         .invalidateHttpSession(true);
    }
 
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) 
            throws Exception 
    {
        auth.inMemoryAuthentication().withUser(userName).password(passwordEndcoder().encode(password)).roles("USER");
    }
    
    @Bean
    PasswordEncoder passwordEndcoder() {
    	return new BCryptPasswordEncoder();
    }
}