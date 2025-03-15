package com.yourssu.application.dto.commentdto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponseDTO {

    private int commentId;
    private String email;
    private String content;

    public CommentResponseDTO(int commentId, String email, String content) {
        this.commentId = commentId;
        this.email = email;
        this.content = content;
    }
}
