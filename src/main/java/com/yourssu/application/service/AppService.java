package com.yourssu.application.service;

import com.yourssu.application.dto.ArticleResponseDTO;
import com.yourssu.application.dto.UserDTO;
import com.yourssu.application.entity.Article;
import com.yourssu.application.entity.User;

public interface AppService {

    // 기능 : 회원 가입, 게시글 작성, 수정, 삭제

    //get 함수
    User getUser(String email);

    Article getArticle(String title);

    // save 함수
    // 회원가입
    UserDTO registerUser(User theUser);

    // 게시글 작성
    ArticleResponseDTO saveArticle(User thUser, String title, String content);

    // 댓글 작성
    void saveComment(String email, String content);

    // merge 함수
    // 게시물 수정
    void mergeArticle(String title);
}
