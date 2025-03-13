package com.yourssu.application.restcontroller;

import com.yourssu.application.entity.User;
import com.yourssu.application.service.AppService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {

    AppService appService;

    public UserRestController(AppService appService) {
        this.appService = appService;
    }

    /*
    @PostMapping("/register")
    public User register(@RequestBody User theUser){
        User tempUser = appService.registerUser(theUser);
        return tempUser;
    }

     */
    @PostMapping("/register")
    public User registerUser(@RequestBody User theUser) {
        appService.registerUser(theUser);
        return theUser;
    }
}
