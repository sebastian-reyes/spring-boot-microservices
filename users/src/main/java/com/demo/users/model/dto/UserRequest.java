package com.demo.users.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String username;
    private String password;
    private String email;
    private Boolean enabled;
    @Transient
    private Boolean admin;

    public Boolean isAdmin() {
        return admin;
    }
}
