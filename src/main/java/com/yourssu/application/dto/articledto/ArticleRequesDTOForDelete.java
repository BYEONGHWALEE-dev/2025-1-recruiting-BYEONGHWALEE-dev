package com.yourssu.application.dto.articledto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ArticleRequesDTOForDelete {

    private String email;
    private String password;

    public ArticleRequesDTOForDelete(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
