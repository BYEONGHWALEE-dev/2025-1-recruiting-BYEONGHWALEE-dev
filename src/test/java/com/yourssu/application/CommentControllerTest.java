package com.yourssu.application;

import com.google.gson.Gson;
import com.yourssu.application.dto.commentdto.CommentRequesDTOForDelete;
import com.yourssu.application.entity.Comment;
import com.yourssu.application.entity.User;
import com.yourssu.application.exceptionhandling.GlobalExceptionHandler;
import com.yourssu.application.restcontroller.CommentController;
import com.yourssu.application.service.AppService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {

    @InjectMocks
    private CommentController commentController;

    @Mock
    private AppService appService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private MockMvc mockMvc;

    private Gson gson = new Gson();

    private final String TEST_EMAIL = "user@test.com";
    private final String TEST_PASSWORD = "password";
    private final String ENCODED_PASSWORD = "$2a$10$encodedPassword";

    private User createTestUser() {
        User user = new User(TEST_EMAIL, ENCODED_PASSWORD, "testUser");
        return user;
    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);  // Mock 객체 초기화
        mockMvc = MockMvcBuilders.standaloneSetup(commentController)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("댓글 삭제 실패 - 댓글 ID가 존재하지 않음")
    void deleteComment_Fail_CommentNotFound() throws Exception {
        // Given
        int articleId = 1;
        int commentId = 99; // 존재하지 않는 ID
        User user = createTestUser();
        CommentRequesDTOForDelete requestDTO = new CommentRequesDTOForDelete(TEST_EMAIL, TEST_PASSWORD);

        when(appService.getUser(TEST_EMAIL)).thenReturn(user);
        when(bCryptPasswordEncoder.matches(TEST_PASSWORD, user.getPassword())).thenReturn(true);
        when(appService.getCommentById(commentId)).thenThrow(new RuntimeException("댓글을 찾을 수 없습니다."));

        // When
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/delete-comment/" + articleId + "/" + commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDTO))
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("댓글을 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("댓글 삭제 실패 - 댓글 작성자가 아님")
    void deleteComment_Fail_NotCommentOwner() throws Exception {
        // Given
        int articleId = 1;
        int commentId = 10;
        User user = createTestUser();
        Comment existingComment = new Comment("원래 있던거");
        user.addComment(existingComment);

        //얘는 실험용
        Comment comment = new Comment("댓글 내용"); // 다른 유저가 작성한 댓글
        CommentRequesDTOForDelete requestDTO = new CommentRequesDTOForDelete(TEST_EMAIL, TEST_PASSWORD);

        when(appService.getUser(TEST_EMAIL)).thenReturn(user);
        when(bCryptPasswordEncoder.matches(TEST_PASSWORD, user.getPassword())).thenReturn(true);
        when(appService.getCommentById(commentId)).thenReturn(comment);

        // When
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/delete-comment/" + articleId + "/" + commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestDTO))
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("댓글을 삭제할 수 없습니다."));



    }
}
