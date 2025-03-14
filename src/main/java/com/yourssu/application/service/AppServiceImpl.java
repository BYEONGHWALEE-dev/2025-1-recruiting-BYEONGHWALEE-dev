package com.yourssu.application.service;

import com.yourssu.application.dao.ArticleRepository;
import com.yourssu.application.dao.CommentRepository;
import com.yourssu.application.dao.UserRepository;
import com.yourssu.application.dto.ArticleResponseDTO;
import com.yourssu.application.dto.UserDTO;
import com.yourssu.application.entity.Article;
import com.yourssu.application.entity.Comment;
import com.yourssu.application.entity.User;
import com.yourssu.application.exceptionhandling.UserNotFoundException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
        if (theUser == null) {
            throw new UserNotFoundException("사용자 정보가 일치하지 않습니다.");
        }
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
