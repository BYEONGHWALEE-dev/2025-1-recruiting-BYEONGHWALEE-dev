package com.yourssu.application.service;

import com.yourssu.application.dao.ArticleRepository;
import com.yourssu.application.dao.CommentRepository;
import com.yourssu.application.dao.UserRepository;
import com.yourssu.application.dto.articledto.ArticleResponseDTO;
import com.yourssu.application.dto.userdto.UserDTO;
import com.yourssu.application.dto.commentdto.CommentResponseDTO;
import com.yourssu.application.entity.Article;
import com.yourssu.application.entity.Comment;
import com.yourssu.application.entity.User;
import com.yourssu.application.exceptionhandling.UserNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppServiceImpl implements AppService{

    private UserRepository userRepository;
    private ArticleRepository articleRepository;
    private CommentRepository commentRepository;

    public AppServiceImpl(
            UserRepository userRepository, ArticleRepository articleRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }

    // get 함수
    @Override
    public User getUser(String email) {
        User theUser = userRepository.findByEmail(email);
        if (theUser == null) {
            throw new UserNotFoundException("사용자 정보가 일치하지 않습니다.");
        }
        return theUser;
    }

    @Override
    public Article getArticleById(int articleId) {
        Optional<Article> theArticle = articleRepository.findById(articleId);
        if(theArticle.isEmpty()) {
            throw new RuntimeException("게시물이 없습니다.");
        }
        return theArticle.get();
    }

    @Override
    public Comment getCommentById(int commentId) {
        Optional<Comment> theComment = commentRepository.findById(commentId);
        if(theComment.isEmpty()) {
            throw new RuntimeException("댓글이 없습니다.");
        }
        return theComment.get();
    }


    //save 함수
    // 회원가입
    @Transactional
    @Override
    public UserDTO registerUser(User theUser) {
        userRepository.save(theUser);
        return new UserDTO(theUser.getEmail(), theUser.getUsername());
    }

    // 게시글 작성
    @Transactional
    @Override
    public ArticleResponseDTO saveArticle(User theUser, String title, String content) {

        Article theArticle = new Article(title, content);
        theUser.addArticle(theArticle);
        theArticle.setUser(theUser);
        articleRepository.save(theArticle);

        return new ArticleResponseDTO(theArticle.getArticleId(), theUser.getEmail(), title, content);
    }

    // 댓글 작성
    @Transactional
    @Override
    public CommentResponseDTO saveComment(Article theArticle, String email, String content) {
        User theUser = getUser(email);
        Comment theComment = new Comment(content, theUser, theArticle);

        theComment.setArticle(theArticle);
        theComment.setUser(theUser);

        commentRepository.save(theComment);

        return new CommentResponseDTO(theComment.getCommentId(), email, content);
    }

    // merge 함수
    // 게시글 수정
    @Transactional
    @Override
    public ArticleResponseDTO mergeArticle(User theUser, int articleId, String theTitle, String theContent) {
        Article theArticle = getArticleById(articleId);

        theArticle.setTitle(theTitle);
        theArticle.setContent(theContent);
        articleRepository.save(theArticle);

        return new ArticleResponseDTO(theArticle.getArticleId(), theUser.getEmail(), theTitle, theContent);
    }

    @Transactional
    @Override
    public CommentResponseDTO updateComment(User theUser, int commentId, String theContent) {
        Comment theComment = getCommentById(commentId);

        theComment.setContent(theContent);
        commentRepository.save(theComment);

        return new CommentResponseDTO(commentId, theUser.getEmail(), theContent);
    }

    // delete 함수
    // Article 삭제
    @Transactional
    @Override
    public Article deleteArticle(int articleId, User theUser) {
        Article theArticle = getArticleById(articleId);
        // 연관성 끊기
        theUser.getArticles().remove(theArticle);

        articleRepository.delete(theArticle);
        return theArticle;
    }

    // comment 삭제
    @Transactional
    @Override
    public Comment deleteComment(int commentId, User theUser, Article theArticle) {
        Comment theComment = getCommentById(commentId);

        // 연관성 끊기
        theUser.getComments().remove(theComment);
        theArticle.getComments().remove(theComment);

        commentRepository.delete(theComment);
        return theComment;
    }


    // 유저 삭제
    @Transactional
    @Override
    public User quitUser(User theUser) {
        userRepository.delete(theUser);
        return theUser;
    }

}
