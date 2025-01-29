package com.demo.authentication.service;

import com.demo.authentication.model.dto.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final WebClient.Builder webClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        try {
            User user = webClient.build()
                    .get()
                    .uri("/username/{username}", params)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(User.class)
                    .block();
            log.info("User: {}", user);
            assert user != null;
            List<GrantedAuthority> roles = user.getRoles().stream()
                    .map(role -> (GrantedAuthority) role::getName)
                    .toList();
            return new org.springframework.security.core.userdetails.User(user.getUsername(),
                    user.getPassword(),
                    user.getEnabled(),
                    true,
                    true,
                    true,
                    roles);
        } catch (WebClientResponseException e) {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
