package com.example.backendservice;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableOAuth2Sso
//@EnableResourceServer
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/anonymous*").anonymous()
            .antMatchers("/login**").permitAll()
           .antMatchers("/callback1**").permitAll()
            .anyRequest().authenticated();
        
        http.cors().disable();
	}
	 

}
