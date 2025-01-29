package com.demo.authentication.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private Long idUser;
    private String username;
    private String password;
    private String email;
    private Boolean enabled;
    private List<Role> roles;
}
