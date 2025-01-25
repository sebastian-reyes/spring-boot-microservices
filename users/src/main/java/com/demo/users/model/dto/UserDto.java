package com.demo.users.model.dto;

import com.demo.users.model.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long idUser;
    private String username;
    private String email;
    private Boolean enabled;
    private List<Role> roles;
}
