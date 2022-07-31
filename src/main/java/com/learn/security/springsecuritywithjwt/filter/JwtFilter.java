package com.learn.security.springsecuritywithjwt.filter;

import com.learn.security.springsecuritywithjwt.service.JwtUtilService;
import com.learn.security.springsecuritywithjwt.service.UserDetailServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    public JwtUtilService jwtUtilService;
    public UserDetailServiceImpl userDetailService;
    private final static String authorizationHeader = "Authorization";
    private final static String tokenPrefix = "Bearer";

    public JwtFilter(JwtUtilService jwtUtilService, UserDetailServiceImpl userDetailService) {
        this.jwtUtilService = jwtUtilService;
        this.userDetailService = userDetailService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(authorizationHeader);

        if (authorization != null && authorization.startsWith(tokenPrefix)) {
            String token = authorization.substring(7);
            String username = jwtUtilService.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailService.loadUserByUsername(username);

                if (jwtUtilService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        } else {
            log.warn("JWT not found OR invalid!");
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
