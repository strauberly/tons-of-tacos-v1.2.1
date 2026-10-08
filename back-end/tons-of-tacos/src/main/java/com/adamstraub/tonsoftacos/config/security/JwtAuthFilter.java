package com.adamstraub.tonsoftacos.config.security;
import com.adamstraub.tonsoftacos.entities.Owner;
import com.adamstraub.tonsoftacos.repository.OwnerRepository;
import com.adamstraub.tonsoftacos.services.security.EncryptionService.IEncryptionService;
import com.adamstraub.tonsoftacos.services.security.JwtService.JwtService;
import com.adamstraub.tonsoftacos.services.security.TokenRefreshService.ITokenRefreshService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
//should be interface?
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final ITokenRefreshService tokenRefreshService;
    @Autowired
    private IEncryptionService encryptionService;
    @Autowired
    private OwnerRepository owners;

@Autowired
@Qualifier("handlerExceptionResolver")
private final HandlerExceptionResolver resolver;



    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain){
        Cookie[] cookies = request.getCookies();
        log.info("cookies from request 1: {}", (Object) request.getCookies());
        log.info("cookies from request 2: {}", Arrays.toString(cookies));
//        log.info("cookies from request: {}", Arrays.stream(cookies).toList());
        log.info("cookies from request 3: {}", (Object) cookies);
        log.info("cookies from request 4 : {}", request.getHeaderNames());
//        log.info("cookies from request 5 : {}", request.getHeaders());

        log.info("cookies exist: {}", cookies != null);

        List<Owner> allOwners = owners.findAll();
        String cookieValue;
        String cookieName = ""; // Match with the cookie name set in Next.js
//        log.info("cookies: {}", cookies);
        if (cookies!= null){
            for(Cookie cookie : cookies){
                log.info("cookie value: {}", cookie.getValue());
//                log.info("cookie name: {}", cookie.getName());
                log.info("cookie name: {}", encryptionService.decrypt(cookie.getName()));
                for (Owner owner: allOwners){
//                    decrypt cookie name
                    if(encryptionService.decrypt(cookie.getName()).equals(owner.getName().substring(0, owner.getName().indexOf(" ")))){
                        cookieValue = cookie.getValue();
                        log.info("cookie: {}", cookieValue);
//                        set needed values for auth and break else throw appropriate exception
                    }
                }
            }
        }
//        // Retrieve the cookies from the request
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//        ** taget cookie name equals owner name from cookie value when decrypted and matches an owner name in the db
//        ** owner name from token should not match and owner name from token decrypted should match in db
//                if (targetCookieName.equals(cookie.getName())) {
//                    cookieValue = cookie.getValue();
//                    break; // Stop searching once the desired cookie is found
//                }
//            }
//        }
//
//        // Prepare the response based on whether the cookie was found
//        if (cookieValue != null) {
//            return ResponseEntity.ok("Value of " + targetCookieName + ": " + cookieValue);
//        } else {
//            return ResponseEntity.status(404).body("Cookie not found.");
//        }
//    }

        System.out.println("jwt filter");
        log.info("from: {}", request.getRequestURL());
        try {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//            if manually set
            String cookie = request.getHeader(HttpHeaders.COOKIE);
            log.error("cookie received, {}", cookie);
            String token = null;
            String username = null;
            String refreshToken = null;
//            should receive cookie and claims extracted?

            if(cookie!= null){
                token = cookie.substring(cookie.indexOf("=") + 1, cookie.length() - 1);
                log.info("cookieToken: {}", token);
                refreshToken = jwtService.extractRefreshToken(token);
                log.info("token1: {}", token);

//            username  = tokenRefreshService.findByToken(token).getOwnerInfo().getUsername();
                username  = tokenRefreshService.findByToken(refreshToken).getOwnerInfo().getUsername();
                log.info("userName: {}", username );
                log.info("userName: {}", encryptionService.decrypt(username));

            }else

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                log.info("token: {}", token);
                username = jwtService.extractUsername(token);
                log.info("userName: {}", username );
                log.info("userName: {}", encryptionService.decrypt(username));
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
//        return username -> ownerRepository.findByUsername(username)
        return username -> owners.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User unauthorized."));

    }
}

