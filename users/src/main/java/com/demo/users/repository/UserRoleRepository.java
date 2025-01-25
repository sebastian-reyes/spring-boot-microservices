package com.demo.users.repository;

import com.demo.users.model.entities.UserRole;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface UserRoleRepository extends R2dbcRepository<UserRole, Long> {
    Flux<UserRole> findAllByIdUser(Long idUser);
}
