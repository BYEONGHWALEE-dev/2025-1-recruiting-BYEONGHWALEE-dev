package com.yourssu.application.restcontroller;

import com.yourssu.application.dto.UserDTO;
import com.yourssu.application.entity.User;
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
}
