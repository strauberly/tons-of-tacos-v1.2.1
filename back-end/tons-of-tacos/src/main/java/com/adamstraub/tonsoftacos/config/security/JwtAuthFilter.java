package com.adamstraub.tonsoftacos.config.security;
import com.adamstraub.tonsoftacos.dao.OwnerRepository;
import com.adamstraub.tonsoftacos.services.security.JwtService;
import com.adamstraub.tonsoftacos.services.security.TokenRefreshService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final OwnerRepository ownerRepository;
    private final TokenRefreshService tokenRefreshService;

@Autowired
@Qualifier("handlerExceptionResolver")
private final HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("jwt filter");
        try {

            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            String cookie = request.getHeader(HttpHeaders.COOKIE);

            String token = null;
            String username = null;
            Date expiration = null;
            Date issuedAt = null;
            if(cookie!= null){
                token = cookie.substring(cookie.indexOf("=") + 1);
            username  = tokenRefreshService.findByToken(token).getOwnerInfo().getUsername();
            }else

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                username = jwtService.extractUsername(token);

            }
            UserDetails userDetails;
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if(cookie!=null) {
                    userDetails = userDetailsService().loadUserByUsername(username);
                }else{
                    userDetails = userDetailsService().loadUserByUsername(jwtService.decrypt(username));
                }
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null
                        , userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            resolver.resolveException(request, response, null, e);

        }
    }

    @Bean
    UserDetailsService userDetailsService(){
        return username -> ownerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User unauthorized."));

    }
}

