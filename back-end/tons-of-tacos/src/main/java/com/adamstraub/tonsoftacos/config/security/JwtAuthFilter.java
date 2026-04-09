package com.adamstraub.tonsoftacos.config.security;
import com.adamstraub.tonsoftacos.repository.OwnerRepository;
import com.adamstraub.tonsoftacos.services.security.EncryptionService.IEncryptionService;
import com.adamstraub.tonsoftacos.services.security.JwtService.JwtService;
import com.adamstraub.tonsoftacos.services.security.TokenRefreshService.ITokenRefreshService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final OwnerRepository ownerRepository;
    @Autowired
    private final ITokenRefreshService tokenRefreshService;
    @Autowired
    private IEncryptionService encryptionService;

@Autowired
@Qualifier("handlerExceptionResolver")
private final HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain)
            {

        System.out.println("jwt filter");
        try {

            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            String cookie = request.getHeader(HttpHeaders.COOKIE);
            log.error("cookie received, {}", cookie);
            String token = null;
            String username = null;

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
                    userDetails = userDetailsService().loadUserByUsername(encryptionService.decrypt(username));
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

