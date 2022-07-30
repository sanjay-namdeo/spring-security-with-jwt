package com.learn.security.springsecuritywithjwt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "build")
public class SignupRequest {
    @NotBlank(message = "Please enter your name")
    private String name;
    @NotBlank(message = "Please enter a valid phone number")
    @Pattern(regexp = "^\\d{10}$", message = "Please enter a valid phone number")
    private String mobile;
    @NotBlank(message = "Please enter a valid email address")
    @Email(message = "Please enter a valid email address")
    private String email;
    @NotBlank(message = "Please enter a username")
    private String username;
    @NotBlank(message = "Please enter password")
    private String password;
}
