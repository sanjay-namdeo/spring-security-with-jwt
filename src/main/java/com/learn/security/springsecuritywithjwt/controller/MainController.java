package com.learn.security.springsecuritywithjwt.controller;

import com.learn.security.springsecuritywithjwt.exception.IncorrectUsernameOrPasswordException;
import com.learn.security.springsecuritywithjwt.exception.UserNotFoundException;
import com.learn.security.springsecuritywithjwt.pojo.LoginRequest;
import com.learn.security.springsecuritywithjwt.service.JwtUtilService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class MainController {

    private final JwtUtilService jwtUtilService;
    private final AuthenticationManager authenticationManager;

    public MainController(JwtUtilService jwtUtilService, AuthenticationManager authenticationManager) {
        this.jwtUtilService = jwtUtilService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public String generateToken(@RequestBody LoginRequest authorizeReq) {
        try {
            log.info("Trying to authentication");
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authorizeReq.getUsername(), authorizeReq.getPassword())
            );
            if (!authenticate.isAuthenticated()) {
                throw new IncorrectUsernameOrPasswordException("Incorrect username or password");
            }
            return jwtUtilService.generateToken(authorizeReq.getUsername());
        } catch (UserNotFoundException e) {
            log.error("Invalid username/password");
            return null;
        }
    }

    @GetMapping("/hello")
    public String sayHello(@RequestParam String name) {
        return "Hello " + name;
    }
}
