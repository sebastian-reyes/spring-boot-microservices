package com.demo.gateway.security;

import com.demo.gateway.util.constants.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange(auth -> auth.pathMatchers("/authorized", "/logout").permitAll()
                        .pathMatchers(HttpMethod.GET, "/gateway/items", "/gateway/products", "/gateway/users").permitAll()
                        .pathMatchers(HttpMethod.GET, "/gateway/items/{id}", "/gateway/products/{id}", "/gateway/users/{id}")
                        .hasAnyRole(Constants.ROLE_ADMIN, Constants.ROLE_USER)
                        .pathMatchers("/gateway/items/**", "/gateway/products/**", "/gateway/users/**")
                        .hasAnyRole(Constants.ROLE_ADMIN)
                        .anyExchange().authenticated())
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .oauth2Login(withDefaults())
                .oauth2Client(withDefaults())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt ->
                        jwt.jwtAuthenticationConverter((Converter<Jwt, Mono<AbstractAuthenticationToken>>)
                                source -> {
                                    Collection<String> roles = source.getClaimAsStringList("roles");
                                    List<SimpleGrantedAuthority> authorities = roles.stream()
                                            .map(SimpleGrantedAuthority::new)
                                            .toList();
                                    return Mono.just(new JwtAuthenticationToken(source, authorities));
                                })))
                .build();
    }
}
