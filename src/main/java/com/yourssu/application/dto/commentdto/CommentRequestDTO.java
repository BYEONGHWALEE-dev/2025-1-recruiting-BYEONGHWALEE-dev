package com.yourssu.application.dto.commentdto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDTO {

    private String email;
    private String password;
    private String content;

    public CommentRequestDTO(String email, String password, String content) {
        this.email = email;
        this.password = password;
        this.content = content;
    }
}
