package com.learn.security.springsecuritywithjwt.filter;

import com.learn.security.springsecuritywithjwt.exception.InvalidJWTException;
import com.learn.security.springsecuritywithjwt.exception.UserNotFoundException;
import com.learn.security.springsecuritywithjwt.service.JwtUtilService;
import com.learn.security.springsecuritywithjwt.service.UserDetailServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    public JwtUtilService jwtUtilService;
    public UserDetailServiceImpl userDetailService;

    public JwtFilter(JwtUtilService jwtUtilService, UserDetailServiceImpl userDetailService) {
        this.jwtUtilService = jwtUtilService;
        this.userDetailService = userDetailService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authorization != null && authorization.startsWith("Bearer")) {
            token = authorization.substring(7);
            username = jwtUtilService.extractUsername(token);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailService.loadUserByUsername(username);

            if (userDetails == null) {
                throw new UserNotFoundException("User has not signed up!");
            }

            if (jwtUtilService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                throw new InvalidJWTException(HttpStatus.BAD_REQUEST);
            }
        }

        filterChain.doFilter(request, response);
    }
}
