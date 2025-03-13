package com.yourssu.application.service;

import com.yourssu.application.dao.ArticleRepository;
import com.yourssu.application.dao.CommentRepository;
import com.yourssu.application.dao.UserRepository;
import com.yourssu.application.entity.Article;
import com.yourssu.application.entity.Comment;
import com.yourssu.application.entity.User;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import javax.swing.text.AbstractDocument;

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
        return theUser;
    }

    @Override
    public Article getArticle(String title) {
        Article theArticle = articleRepository.findByTitle(title);
        return theArticle;
    }


    //save 함수
    // 회원가입
    @Transactional
    @Override
    public User registerUser(String email, String password, String username) {
        User theUser = new User(email, password, username);
        userRepository.save(theUser);

        return theUser;
    }

    @Transactional
    @Override
    public User registerUser(User theUser) {
        userRepository.save(theUser);
        return theUser;
    }

    // 게시글 작성
    @Transactional
    @Override
    public void saveArticle(String email, String title, String content) {
        Article theArticle = new Article(title, content);
        getUser(email).addArticle(theArticle);
        articleRepository.save(theArticle);
    }

    // 댓글 작성
    @Transactional
    @Override
    public void saveComment(String email, String content) {
        User theUser = getUser(email);
        Comment theComment = new Comment(content);
        theUser.addComment(theComment);

        commentRepository.save(theComment);
    }

    //merge 함수
    @Transactional
    @Override
    public void mergeArticle(String title) {

    }

}
