package com.yourssu.application;

import com.yourssu.application.entity.Article;
import com.yourssu.application.entity.User;
import com.yourssu.application.exceptionhandling.GlobalExceptionHandler;
import com.yourssu.application.restcontroller.ArticleController;
import com.yourssu.application.service.AppService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ArticleControllerTest {

    @InjectMocks
    private ArticleController articleController;

    @Mock
    private AppService appService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);  // Mock 객체 초기화
        mockMvc = MockMvcBuilders.standaloneSetup(articleController)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @DisplayName("게시글이 유저의 게시글 목록에 없을 때 - checkArticleInUser")
    @Test
    void checkArticleInUser_Fail_ArticleNotFound() {
        // Given
        String email = "user@test.com";

        User user = new User(email, "$2a$10$encodedPassword", "username");
        user.addArticle(new Article("1", "2"));
        Article existingArticle = new Article("randomTitle", "randomContent");

        // When & Then
        // 예외가 발생해야 함
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            articleController.checkArticleInUser(user, existingArticle); // 해당 게시글이 유저의 목록에 없을 때
        });

        // 예외 메시지가 맞는지 확인
        assertEquals("게시글을 찾을 수 없습니다.", exception.getMessage());

        // 이메일 비번 확인은 앞의 usertestcode에서 완성했기 때문에 따로 하지 않음
    }
}
