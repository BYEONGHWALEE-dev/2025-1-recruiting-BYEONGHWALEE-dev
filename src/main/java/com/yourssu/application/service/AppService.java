package com.yourssu.application.service;

import com.yourssu.application.entity.Article;
import com.yourssu.application.entity.User;
import org.springframework.stereotype.Service;

public interface AppService {

    // 기능 : 회원 가입, 게시글 작성, 수정, 삭제

    //get 함수
    User getUser(String email);

    Article getArticle(String title);

    // save 함수
    // 회원가입
    User registerUser(String username, String password, String email);

    User registerUser(User theUser);

    // 게시글 작성
    void saveArticle(String email, String title, String content);

    // 댓글 작성
    void saveComment(String email, String content);

    // merge 함수
    // 게시물 수정
    void mergeArticle(String title);
}
