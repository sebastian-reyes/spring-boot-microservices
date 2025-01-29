package com.demo.users.services.impl;

import com.demo.users.model.dto.UserDto;
import com.demo.users.model.dto.UserLoginDto;
import com.demo.users.model.dto.UserRequest;
import com.demo.users.model.entities.Role;
import com.demo.users.model.entities.User;
import com.demo.users.model.entities.UserRole;
import com.demo.users.repository.RoleRepository;
import com.demo.users.repository.UserRepository;
import com.demo.users.repository.UserRoleRepository;
import com.demo.users.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    @Override
    public Flux<UserDto> findAll() {
        return userRepository.findAll()
                .concatMap (user ->
                    userRoleRepository.findAllByIdUser(user.getIdUser())
                            .flatMap(userRole -> roleRepository.findById(userRole.getIdRole()))
                            .collectList()
                            .switchIfEmpty(Mono.just(Collections.emptyList()))
                            .map(roles -> userToUserDto(user, roles))
                );

    }

    @Override
    public Mono<UserDto> findById(Long id) {
        return userRepository.findById(id)
                .flatMap(user ->
                        userRoleRepository.findAllByIdUser(id)
                                .flatMap(userRole -> roleRepository.findById(userRole.getIdRole()))
                                .collectList()
                                .map(roles -> userToUserDto(user, roles))
                );
    }

    @Override
    public Mono<UserDto> save(UserRequest userDto) {
        User newUser = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .enabled(userDto.getEnabled())
                .build();
        return userRepository.save(newUser)
                .flatMap(savedUser -> {
                    String roleName = (userDto.isAdmin() != null && userDto.isAdmin())
                            ? "ROLE_ADMIN"
                            : "ROLE_USER";
                    return roleRepository.findByName(roleName)
                            .switchIfEmpty(Mono.error(new RuntimeException("Role not found: " + roleName)))
                            .flatMap(role ->
                                    userRoleRepository.save(new UserRole(null, savedUser.getIdUser(), role.getIdRole()))
                            )
                            .thenReturn(savedUser);
                })
                .flatMap(savedUser ->
                        userRoleRepository.findAllByIdUser(savedUser.getIdUser())
                                .flatMap(userRole -> roleRepository.findById(userRole.getIdRole()))
                                .collectList()
                                .map(roles -> userToUserDto(savedUser, roles)));
    }

    @Override
    public Mono<User> update(Long id, User user) {
        return userRepository.findById(id)
                .flatMap(existingUser -> {
                    log.info(existingUser.toString());
                    existingUser.setUsername(user.getUsername());
                    if (user.getPassword() != null && !user.getPassword().isBlank()) {
                        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
                    }
                    existingUser.setEmail(user.getEmail());
                    existingUser.setEnabled(user.getEnabled());
                    return userRepository.save(existingUser);
                });
    }

    @Override
    public Mono<Void> delete(Long id) {
        return userRepository.deleteById(id);
    }

    @Override
    public Mono<UserLoginDto> findByUsername(String username) {
        return userRepository.findByUsername(username).flatMap(user ->
                userRoleRepository.findAllByIdUser(user.getIdUser())
                        .flatMap(userRole -> roleRepository.findById(userRole.getIdRole()))
                        .collectList()
                        .map(roles -> getUserDetailsToLogin(user, roles))
        );
    }

    private UserDto userToUserDto(User user, List<Role> roles) {
        return UserDto.builder()
                .idUser(user.getIdUser())
                .username(user.getUsername())
                .email(user.getEmail())
                .enabled(user.getEnabled())
                .roles(roles != null ? roles : Collections.emptyList())
                .build();
    }

    private UserLoginDto getUserDetailsToLogin(User user, List<Role> roles) {
        return UserLoginDto.builder()
                .idUser(user.getIdUser())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .enabled(user.getEnabled())
                .roles(roles != null ? roles : Collections.emptyList())
                .build();
    }
}
