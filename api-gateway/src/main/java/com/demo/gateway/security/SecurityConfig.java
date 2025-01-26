package com.demo.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange(auth -> auth.pathMatchers("/authorized", "/logout").permitAll()
                .pathMatchers(HttpMethod.GET, "gateway/items", "gateway/products", "gateway/users").permitAll()
                .pathMatchers(HttpMethod.GET, "gateway/items/{id}", "gateway/products/{id}", "gateway/users/{id}").hasAnyRole("USER", "ADMIN")
                .pathMatchers("gateway/items/**", "gateway/products/**", "gateway/users/**").hasRole("ADMIN")
                .anyExchange().authenticated()).cors(csrf -> csrf.disable())
                .csrf(csrf -> csrf.disable())
                .oauth2Login(withDefaults())
                .oauth2Client(withDefaults())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                .build();
    }
}
