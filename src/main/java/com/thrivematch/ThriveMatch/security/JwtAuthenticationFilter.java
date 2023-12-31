package com.thrivematch.ThriveMatch.security;

import java.io.IOException;


import com.thrivematch.ThriveMatch.model.UserType;
import com.thrivematch.ThriveMatch.repository.TokenRepo;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.validation.constraints.NotNull;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private TokenRepo tokenRepo;

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain)
            throws ServletException, IOException {
        String token = getJWTfromRequest(request);
        var isTokenValid = tokenRepo.findByToken(token)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);
        if (token != null && jwtGenerator.validateToken(token) && isTokenValid) {
            String username = jwtGenerator.getUsernameFromJWT(token);
            String userType = jwtGenerator.getUserTypeFromJWT(token);
            customUserDetailsService.setUserType(UserType.valueOf(userType));
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                    null, userDetails.getAuthorities());

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);

    }

    private String getJWTfromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        } else {
            return null;
        }
    }
}