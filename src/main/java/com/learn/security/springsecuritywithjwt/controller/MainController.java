package com.learn.security.springsecuritywithjwt.controller;

import com.learn.security.springsecuritywithjwt.pojo.LoginRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        return loginRequest.toString();
    }
}
