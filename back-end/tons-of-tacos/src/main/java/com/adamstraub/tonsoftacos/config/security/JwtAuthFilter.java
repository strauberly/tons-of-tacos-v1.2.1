package com.adamstraub.tonsoftacos.config.security;
import com.adamstraub.tonsoftacos.dao.OwnerRepository;
import com.adamstraub.tonsoftacos.services.security.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
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
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final OwnerRepository ownerRepository;

@Autowired
@Qualifier("handlerExceptionResolver")
private final HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("jwt filter");
        try {
//            System.out.println("request: " + request);
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        System.out.println("auth header: " + authHeader);
            String token = null;
            String username = null;
            Date expiration = null;
            Date issuedAt = null;

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
//                System.out.println("token = " + token);
                username = jwtService.extractUsername(token);
//                System.out.println("username = " + username);
                expiration = jwtService.extractExpiration(token);
//                System.out.println("expiration = " + expiration);
                issuedAt = jwtService.extractIssuedAt(token);
//                System.out.println("issued at = " + issuedAt);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService().loadUserByUsername(jwtService.decrypt(username));
//                System.out.println("user details: " + userDetails);
//
//                System.out.println("token valid: " + jwtService.isTokenValid(token, userDetails));
                jwtService.isTokenValid(token, userDetails);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null
                        , userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
//                System.out.println("authToken: " + authToken);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
//            System.out.println(e);
            resolver.resolveException(request, response, null, e);
        }
    }

    @Bean
    UserDetailsService userDetailsService(){
        return username -> ownerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User unauthorized."));

    }
}

