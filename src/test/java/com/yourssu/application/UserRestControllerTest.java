package com.yourssu.application;

import com.google.gson.Gson;
import com.yourssu.application.dto.userdto.UserDTO;
import com.yourssu.application.dto.userdto.UserDTOForQuit;
import com.yourssu.application.entity.User;
import com.yourssu.application.exceptionhandling.GlobalExceptionHandler;
import com.yourssu.application.exceptionhandling.UserNotFoundException;
import com.yourssu.application.restcontroller.UserRestController;
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

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserRestControllerTest {

    @InjectMocks
    private UserRestController userRestController;

    @Mock
    private AppService appService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);  // Mock 객체 초기화
        mockMvc = MockMvcBuilders.standaloneSetup(userRestController)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @DisplayName("회원가입 성공")
    @Test
    void registerSuccess() throws Exception {

        // given
        UserDTO theUserDTO = new UserDTO("test@test.com", "username");

        when(appService.registerUser(any(User.class))).thenReturn(theUserDTO);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(theUserDTO))
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(theUserDTO.getEmail()))
                .andExpect(jsonPath("$.username").value(theUserDTO.getUsername()));
    }

    @DisplayName("회원 탈퇴 성공")
    @Test
    void quitUserSuccess() throws Exception {
        // Given
        String email = "test@test.com";
        String rawPassword = "password";
        String encodedPassword = "$2a$10$abcdefghijklmnopqrstuv"; // BCrypt로 암호화된 가짜 비밀번호

        User existingUser = new User(email, encodedPassword, "username");
        UserDTOForQuit request = new UserDTOForQuit(email, rawPassword);

        // Mocking
        when(appService.getUser(email)).thenReturn(existingUser);
        when(bCryptPasswordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);


        doAnswer(invocation -> null).when(appService).quitUser(existingUser);

        // When
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/quit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(new Gson().toJson(request))
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$").value(containsString(email)));
    }

    @DisplayName("회원 탈퇴 실패 - 이메일 불일치")
    @Test
    void quitUserFail_EmailNotFound() throws Exception {
        // Given
        String email = "nonexistent@test.com";
        String password = "password";
        String encodedPassword = "$2a$10$abcdefghijklmnopqrstuv";

        UserDTOForQuit request = new UserDTOForQuit(email, password);

        // Mocking
        when(appService.getUser(email)).thenThrow(new UserNotFoundException("사용자 정보가 일치하지 않습니다."));

        // When
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/quit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        // Then
        resultActions
                .andDo(print())
                .andExpect(status().isNotFound())  // 이메일이 존재하지 않아서 404 오류가 발생해야 합니다.
                .andExpect(jsonPath("$.message").value("사용자 정보가 일치하지 않습니다."));  // 예외 메시지 확인
    }

    @DisplayName("회원 탈퇴 실패 - 이메일 불일치")
    @Test
    void quitUserFail_WrongPassword() throws Exception {
        // Given
        String email = "test@test.com";
        String wrongPassword = "wrongPassword";
        String encodedPassword = "$2a$10$abcdefghijklmnopqrstuv"; // 암호화된 비밀번호

        User existingUser = new User(email, encodedPassword, "username");
        UserDTOForQuit request = new UserDTOForQuit(email, wrongPassword);


        // Mocking
        when(appService.getUser(email)).thenReturn(existingUser);
        when(bCryptPasswordEncoder.matches(wrongPassword, existingUser.getPassword())).thenReturn(false);

        // When
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/quit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        // Then
        resultActions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("사용자 정보가 일치하지 않습니다."));

    }
}
