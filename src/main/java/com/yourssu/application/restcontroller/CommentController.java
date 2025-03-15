package com.yourssu.application.restcontroller;

import com.yourssu.application.dto.articledto.ArticleRequesDTOForDelete;
import com.yourssu.application.dto.commentdto.CommentRequesDTOForDelete;
import com.yourssu.application.dto.commentdto.CommentRequestDTO;
import com.yourssu.application.dto.commentdto.CommentResponseDTO;
import com.yourssu.application.entity.Article;
import com.yourssu.application.entity.Comment;
import com.yourssu.application.entity.User;
import com.yourssu.application.exceptionhandling.UserNotFoundException;
import com.yourssu.application.service.AppService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {

    AppService appService;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public CommentController(AppService appService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.appService = appService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    // 댓글 작성하기
    @PostMapping("/write-comment/{articleId}")
    public CommentResponseDTO writeComment(@PathVariable int articleId, @RequestBody CommentRequestDTO commentRequestDTO) {

        // 1. 이메일이 DB에 있는지 확인(서비스 레이어에서 처리)
        String theEmail = commentRequestDTO.getEmail();
        User theUser = appService.getUser(theEmail);

        // 2. 비밀번호 확인
        String thePassword = commentRequestDTO.getPassword();
        matchPassword(theUser, thePassword);

        // 3. 댓글 작성하기
        Article theArticle = appService.getArticleById(articleId);
        String theContent = commentRequestDTO.getContent();

        return appService.saveComment(theArticle, theEmail, theContent);
    }

    // 댓글 수정하기
    @PostMapping("/modify-comment/{commentId}")
    public CommentResponseDTO modifyComment( @PathVariable("commentId") int commentId, @RequestBody CommentRequestDTO commentRequestDTO) {

        // 1. 이메일 검증(서비스 레이어에서 처리)
        String theEmail = commentRequestDTO.getEmail();
        User theUser = appService.getUser(theEmail);

        // 2. 비밀번호 검증(컨트롤러 레이어에서 검증)
        String thePassword = commentRequestDTO.getPassword();
        matchPassword(theUser, thePassword);

        // 3 user의 commnet 리스트에 해당 comment가 있는지 확인(컨트롤러 레이어에서 검증), 과 동시에 comment가 데이터 베이스에 있는 지 확인
        Comment theComment = appService.getCommentById(commentId); // 여기서 DB 내에 검증
        checkCommentInUser(theUser, theComment);

        // 5. 댓글 수정
        String theContent = commentRequestDTO.getContent();

        return appService.updateComment(theUser, commentId, theContent);
    }

    // 댓글 삭제하기
    @PostMapping("/delete-comment/{articleId}/{commentId}")
    public String deleteComment(@PathVariable("articleId") int articleId, @PathVariable("commentId") int commentId, @RequestBody CommentRequesDTOForDelete commentRequesDTOForDelete) {

        // 1. 이메일 검증
        String theEmail = commentRequesDTOForDelete.getEmail();
        User theUser = appService.getUser(theEmail);

        // 2. 비밀번호 검증(컨트롤러 레이어에서)
        String thePassword = commentRequesDTOForDelete.getPassword();
        matchPassword(theUser, thePassword);

        // 3. user의 commnet 리스트에 해당 comment가 있는지 확인(컨트롤러 레이어에서 검증), 과 동시에 comment가 데이터 베이스에 있는 지 확인
        Comment theComment = appService.getCommentById(commentId);
        checkCommentInUser(theUser, theComment);

        // 4. 댓글 삭제
        Article theArticle = appService.getArticleById(articleId);
        theComment = appService.deleteComment(commentId, theUser, theArticle);

        return theComment.getContent() + " 이 삭제되었습니다.";

    }







    // 비밀번호 검증
    public void matchPassword(User theUser, String thePassword) {
        boolean passwordMatch = bCryptPasswordEncoder.matches(thePassword, theUser.getPassword());
        if (!passwordMatch) {
            throw new UserNotFoundException("사용자 정보가 일치하지 않습니다.");
        }
    }

    // user의 commnet 리스트에 해당 Comment가 있는지 확인(컨트롤러 레이어에서 검증)
    public void checkCommentInUser(User theUser, Comment theComment) {
        if(!theUser.getComments().contains(theComment)) {
            throw new RuntimeException("댓글을 삭제할 수 없습니다.");
        }
    }
}
