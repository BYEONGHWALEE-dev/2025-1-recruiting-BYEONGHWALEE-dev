package com.yourssu.application.dto.commentdto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CommentRequesDTOForDelete {

    private String email;
    private String password;

    public CommentRequesDTOForDelete(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
