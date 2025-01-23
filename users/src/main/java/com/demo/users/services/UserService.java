package com.demo.users.services;

import com.demo.users.model.entities.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Flux<User> findAll();
    Mono<User> findById(Long id);
    Mono<User> save(User user);
    Mono<Void> delete(Long id);
    Mono<User> findByUsername(String username);
}
