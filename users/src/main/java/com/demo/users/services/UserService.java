package com.demo.users.services;

import com.demo.users.model.dto.UserDto;
import com.demo.users.model.dto.UserRequest;
import com.demo.users.model.entities.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Flux<UserDto> findAll();
    Mono<UserDto> findById(Long id);
    Mono<UserDto> save(UserRequest user);
    Mono<User> update(Long id, User user);
    Mono<Void> delete(Long id);
    Mono<UserDto> findByUsername(String username);
}
