package com.yourssu.application.dto;

import com.yourssu.application.entity.Article;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleRequestDTO {

    private String email;
    private String password;
    private String title;
    private String content;

    public ArticleRequestDTO(String email, String password, String title, String content) {
        this.email = email;
        this.password = password;
        this.title = title;
        this.content = content;
    }
}
