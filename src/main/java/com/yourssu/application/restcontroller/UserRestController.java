package com.yourssu.application.restcontroller;

import com.yourssu.application.dto.userdto.UserDTO;
import com.yourssu.application.dto.userdto.UserDTOForQuit;
import com.yourssu.application.entity.User;
import com.yourssu.application.exceptionhandling.UserNotFoundException;
import com.yourssu.application.service.AppService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserRestController {

    AppService appService;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserRestController(AppService appService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.appService = appService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    // 등록 해봅시다.
    @PostMapping("/register")
    public UserDTO registerUser(@RequestBody User theUser) {
        // 비밀본호 BCrypt를 사용해서 인코딩
        theUser.setPassword(bCryptPasswordEncoder.encode(theUser.getPassword()));
        return appService.registerUser(theUser);
    }

    // 탈퇴 해봅시다.
    @PostMapping("/quit")
    public String quitUser(@RequestBody UserDTOForQuit userDTOForQuit) {

        // 1. 이메일 검증(서비스 레이어에서)
        String theEmail = userDTOForQuit.getEmail();
        User theUser = appService.getUser(theEmail);

        // 2. 비밀번호 검증(컨트롤러 레이어에서)
        String thePassword = userDTOForQuit.getPassword();
        boolean passwordMatch = bCryptPasswordEncoder.matches(thePassword, theUser.getPassword());
        if (!passwordMatch) {
            throw new UserNotFoundException("사용자 정보가 일치하지 않습니다.");
        }

        // 3. 회원탈퇴
        appService.quitUser(theUser);

        return theUser.getEmail() + " 님이 탈퇴하였습니다.";
    }

    // 빅데이터분석 과제를 위한 api
    @GetMapping("/api/checkAuthenticate")
    public ResponseEntity<?> getUserInfo(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        // Authorization 헤더에서 Bearer 토큰을 추출
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Unauthorized: No or Invalid Token");
        }

        // Spring Security가 자동으로 관리하는 인증 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증되지 않았을 경우 처리
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized: Not authenticated");
        }

        // 인증된 사용자의 정보 가져오기
        Object principal = authentication.getPrincipal();
        if (principal instanceof Jwt) {
            Jwt jwt = (Jwt) principal;

            // JWT 클레임에서 사용자 정보 추출
            return ResponseEntity.ok(jwt.getClaims());  // JWT 클레임을 응답 본문으로 반환
        } else {
            return ResponseEntity.status(401).body("Unauthorized: Invalid JWT token");
        }
    }
}
