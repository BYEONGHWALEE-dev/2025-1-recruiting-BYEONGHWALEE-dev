package com.yourssu.application;

import com.yourssu.application.entity.User;
import com.yourssu.application.service.AppService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /*
    @Bean
    CommandLineRunner commandLineRunner(AppService appService) {
        return args -> {
            User theUser = new User("asdf@naver.com", "asdf", "asdf");
            appService.registerUser(theUser);
            appService.getUser("asdf@naver.com");
        };
    }

     */
}
