package com.yourssu.application.restcontroller;

import com.yourssu.application.dto.ArticleRequestDTO;
import com.yourssu.application.dto.ArticleResponseDTO;
import com.yourssu.application.entity.Article;
import com.yourssu.application.entity.User;
import com.yourssu.application.exceptionhandling.UserNotFoundException;
import com.yourssu.application.service.AppService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleController {

    AppService appService;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public ArticleController(AppService appService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.appService = appService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/write-article")
    public ArticleResponseDTO writeArticle(@RequestBody ArticleRequestDTO articleRequestDTO) {

        String theTitle = articleRequestDTO.getTitle();
        String theContent = articleRequestDTO.getContent();

        // 1. 이메일 유효성 확인(서비스 레이어에서 처리)
        String theEmail  = articleRequestDTO.getEmail();
        String thePassword = articleRequestDTO.getPassword();
        User theUser = appService.getUser(theEmail);

        // 2. 비밀번호 검증(컨트롤러에서 처리)
        boolean passwordMatch = bCryptPasswordEncoder.matches(thePassword, theUser.getPassword());
        if(!passwordMatch) {
            throw new UserNotFoundException("사용자 정보가 일치하지 않습니다.");
        }

        return appService.saveArticle(theUser, theTitle, theContent);
    }
}
