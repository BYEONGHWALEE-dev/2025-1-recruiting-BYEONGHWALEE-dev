package com.yourssu.application.dto;

import com.yourssu.application.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private String email;
    private String username;

    public UserDTO() {}
    public UserDTO(String email, String username) {
        this.email = email;
        this.username = username;
    }
}
