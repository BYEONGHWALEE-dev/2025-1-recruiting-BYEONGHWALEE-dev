package com.yourssu.application.service;

import com.yourssu.application.dto.articledto.ArticleResponseDTO;
import com.yourssu.application.dto.userdto.UserDTO;
import com.yourssu.application.dto.commentdto.CommentResponseDTO;
import com.yourssu.application.entity.Article;
import com.yourssu.application.entity.Comment;
import com.yourssu.application.entity.User;

public interface AppService {

    // 기능 : 회원 가입, 게시글 작성, 수정, 삭제

    //get 함수
    User getUser(String email);

    Article getArticleById(int articleId);

    Comment getCommentById(int commentId);

    // save 함수
    // 회원가입
    UserDTO registerUser(User theUser);

    // 게시글 작성
    ArticleResponseDTO saveArticle(User thUser, String title, String content);

    // 댓글 작성
    CommentResponseDTO saveComment(Article theArticle, String email, String content);

    // update 함수
    // 게시물 수정
    ArticleResponseDTO mergeArticle(User theUser, int articleId, String theTitle, String theContent);

    // 댓글 수정
    CommentResponseDTO updateComment(User theUser, int commentId, String theContent);

    // delete 함수
    // 게시물 삭제
    Article deleteArticle(int articleId, User theUser);

    // 댓글 삭제
    Comment deleteComment(int commentId, User theUser, Article theArticle);

    // 회원탈퇴
    User quitUser(User theUser);

}
