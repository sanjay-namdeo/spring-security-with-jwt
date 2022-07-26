package com.learn.security.springsecuritywithjwt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "build")
public class SignupResponse {
    private String name;
    private String username;
    private String email;
    private String mobile;
}
