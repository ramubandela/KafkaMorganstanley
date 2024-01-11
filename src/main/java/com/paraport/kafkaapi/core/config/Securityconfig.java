package com.paraport.kafkaapi.core.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context. annotation.Configuration;
import org.springframework.context. annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@Profile("!test")
public class Securityconfig{

@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
http.csrf).disable()
authorizeRequests(
anthatchers(" html actuator/health h2-console permitEAII()
anthatchers(Httphethod.OPTIONS permitAI1I
.anyRequest(.authenticated(
.and
Oauth2Resourceserver( . jut
http.headers(J. fr ameOptions().disable()
return http.buil1df);
}