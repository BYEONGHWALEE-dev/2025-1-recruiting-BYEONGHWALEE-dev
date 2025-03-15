package com.yourssu.application.dto.articledto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleResponseDTO {

    private int articleId;
    private String email;
    private String title;
    private String content;

    public ArticleResponseDTO(int articleId, String email, String title, String content) {
        this.articleId = articleId;
        this.email = email;
        this.title = title;
        this.content = content;
    }
}
