package com.yourssu.application.dto.userdto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTOForQuit {

    private String email;
    private String password;

    public UserDTOForQuit(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
