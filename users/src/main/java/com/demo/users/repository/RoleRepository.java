package com.demo.users.repository;

import com.demo.users.model.entities.Role;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface RoleRepository extends R2dbcRepository<Role, Long> {
}
