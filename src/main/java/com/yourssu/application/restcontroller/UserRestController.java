package com.yourssu.application.restcontroller;

import com.yourssu.application.dto.userdto.UserDTO;
import com.yourssu.application.dto.userdto.UserDTOForQuit;
import com.yourssu.application.entity.User;
import com.yourssu.application.exceptionhandling.UserNotFoundException;
import com.yourssu.application.service.AppService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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


}
