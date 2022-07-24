package com.learn.security.springsecuritywithjwt.controller;

import com.learn.security.springsecuritywithjwt.pojo.LoginRequest;
import com.learn.security.springsecuritywithjwt.service.JwtUtilService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    private final JwtUtilService jwtUtilService;
    private final AuthenticationManager authenticationManager;

    public MainController(JwtUtilService jwtUtilService, AuthenticationManager authenticationManager) {
        this.jwtUtilService = jwtUtilService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public String generateToken(@RequestBody LoginRequest authorizeReq) throws Exception {
        try {
            System.out.println("Trying to authentication");
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authorizeReq.getUsername(), authorizeReq.getPassword())
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Invalid username/password");
        }
        return jwtUtilService.generateToken(authorizeReq.getUsername());
    }
}
