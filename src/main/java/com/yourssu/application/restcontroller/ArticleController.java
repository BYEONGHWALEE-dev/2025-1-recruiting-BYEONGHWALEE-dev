package com.yourssu.application.restcontroller;

import com.yourssu.application.dto.articledto.ArticleRequesDTOForDelete;
import com.yourssu.application.dto.articledto.ArticleRequestDTO;
import com.yourssu.application.dto.articledto.ArticleResponseDTO;
import com.yourssu.application.entity.Article;
import com.yourssu.application.entity.User;
import com.yourssu.application.exceptionhandling.UserNotFoundException;
import com.yourssu.application.service.AppService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
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
    // 게시글 작성하기
    @PostMapping("/write-article")
    public ArticleResponseDTO writeArticle(@RequestBody ArticleRequestDTO articleRequestDTO) {

        String theTitle = articleRequestDTO.getTitle();
        String theContent = articleRequestDTO.getContent();

        // 1. 이메일 유효성 확인(서비스 레이어에서 처리)
        String theEmail  = articleRequestDTO.getEmail();
        User theUser = appService.getUser(theEmail);

        // 2. 비밀번호 검증(컨트롤러에서 처리)
        String thePassword = articleRequestDTO.getPassword();
        matchPassword(theUser, thePassword);


        return appService.saveArticle(theUser, theTitle, theContent);
    }

    // 게시글 수정하기
    @PostMapping("/modify-article/{articleId}")
    public ArticleResponseDTO modifyArticle(@PathVariable int articleId, @RequestBody ArticleRequestDTO articleRequestDTO) {

        String theTitle = articleRequestDTO.getTitle();
        String theContent = articleRequestDTO.getContent();

        // 1. 이메일 유효성 확인(서비스 레이어에서 처리)
        String theEmail = articleRequestDTO.getEmail();
        User theUser = appService.getUser(theEmail);

        // 2. 비밀번호 검증(컨트롤러에서 처리)
        String thePassword = articleRequestDTO.getPassword();
        matchPassword(theUser, thePassword);

        // 3. id가 전체 article 데이터베이스에 있는지 확인(서비스 레이어에서 확인)
        Article theArticle = appService.getArticleById(articleId);

        // 4. article이 해당 user의 article인지 확인(컨트롤러 레이어에서 확인)
        checkArticleInUser(theUser, theArticle);

        // 다 통과됐다면 게시글 수정
        return appService.mergeArticle(theUser, articleId, theTitle, theContent);
    }

    // 게시글 삭제하기
    @PostMapping("/delete-article/{articleId}")
    public String deleteArticle(@PathVariable int articleId, @RequestBody ArticleRequesDTOForDelete articleRequestDTO) {

        // 1. 이메일 검증(서비스 레이어에서 처리)
        String theEmail = articleRequestDTO.getEmail();
        User theUser = appService.getUser(theEmail);

        // 2. 비밀번호 검증(컴트롤러에서 처리)
        String thePassword = articleRequestDTO.getPassword();
        matchPassword(theUser, thePassword);

        // 3. article의 주인이 해당 유저의 Article이 맞는지 확인
        Article theArticle = appService.getArticleById(articleId);
        checkArticleInUser(theUser, theArticle);

        // 맞으면 삭제
        Article deletedArticle = appService.deleteArticle(articleId, theUser);

        return deletedArticle.getTitle() + "게시물이 삭제되었습니다.";
    }





    // Method
    // 비밀번호 검증
    public void matchPassword(User theUser, String thePassword) {
        boolean passwordMatch = bCryptPasswordEncoder.matches(thePassword, theUser.getPassword());
        if (!passwordMatch) {
            throw new UserNotFoundException("사용자 정보가 일치하지 않습니다.");
        }
    }

    // article이 해당 user의 article인지 확인
    public void checkArticleInUser(User theUser, Article theArticle) {
        if (!theUser.getArticles().contains(theArticle)) {
            throw new RuntimeException("게시글을 찾을 수 없습니다.");
        }
    }
}
