package com.example.ecommercemarketplace.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService customUserDetailsService;

    private final static String AUTHORIZATION_HEADER = "Authorization";
    private final static String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(AUTHORIZATION_HEADER);

        if (header == null || !header.startsWith(BEARER_PREFIX)){
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = header.substring(7);
        String username = jwtService.extractEmail(jwt);

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
             UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

             if (jwtService.isValid(jwt)){
                 UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                         userDetails.getUsername(), null, userDetails.getAuthorities()
                 );

                 SecurityContextHolder.getContext().setAuthentication(token);
             }
        }

        filterChain.doFilter(request, response);
    }
}
