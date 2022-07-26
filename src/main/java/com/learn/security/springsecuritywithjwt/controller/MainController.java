package com.learn.security.springsecuritywithjwt.controller;

import com.learn.security.springsecuritywithjwt.exception.IncorrectUsernameOrPasswordException;
import com.learn.security.springsecuritywithjwt.exception.UserNotFoundException;
import com.learn.security.springsecuritywithjwt.pojo.LoginRequest;
import com.learn.security.springsecuritywithjwt.pojo.SignupRequest;
import com.learn.security.springsecuritywithjwt.pojo.SignupResponse;
import com.learn.security.springsecuritywithjwt.service.JwtUtilService;
import com.learn.security.springsecuritywithjwt.service.UserDetailServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
public class MainController {

    private final JwtUtilService jwtUtilService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailServiceImpl userDetailService;

    public MainController(JwtUtilService jwtUtilService, AuthenticationManager authenticationManager, UserDetailServiceImpl userDetailService) {
        this.jwtUtilService = jwtUtilService;
        this.authenticationManager = authenticationManager;
        this.userDetailService = userDetailService;
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

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody @Valid SignupRequest signupRequest) {
        SignupResponse signupResponse = this.userDetailService.saveUser(signupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(signupResponse);
    }
}
