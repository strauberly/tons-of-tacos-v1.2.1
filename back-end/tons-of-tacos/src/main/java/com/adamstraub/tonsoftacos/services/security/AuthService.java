package com.adamstraub.tonsoftacos.services.security;
import com.adamstraub.tonsoftacos.dao.OwnerRepository;
import com.adamstraub.tonsoftacos.dao.RefreshTokenRepository;
import com.adamstraub.tonsoftacos.dto.businessDto.ResponseMessage;
import com.adamstraub.tonsoftacos.dto.businessDto.security.*;
import com.adamstraub.tonsoftacos.entities.Owner;
import com.adamstraub.tonsoftacos.entities.RefreshToken;
import com.adamstraub.tonsoftacos.exceptionHandler.GlobalExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;


//service pertains to authentication functions(login, logout, session timeout etc.)
@Service
@RequiredArgsConstructor
public class AuthService {
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @Autowired
    private final JwtService jwtService;
    private final TokenRefreshService tokenRefreshService;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final OwnerRepository ownerRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final Token token = new Token();
    private  final JwtResponse jwtResponse = new JwtResponse();

    RefreshToken refreshToken;


    public JwtResponse ownerLogin(@NotNull HttpServletRequest request, OwnerAuth ownerAuth) {

        String name;
        System.out.println("auth service");
        System.out.println(ownerAuth);
        System.out.println(jwtService.decrypt(ownerAuth.getUsername()));
        System.out.println(jwtService.decrypt(ownerAuth.getPsswrd()));

        Subject subject;
        try{
           authenticationManager
                  .authenticate(new UsernamePasswordAuthenticationToken(jwtService.decrypt(ownerAuth.getUsername()),
                          jwtService.decrypt(ownerAuth.getPsswrd())));
          } catch (Exception e) {
              logger.error("{} : {} : {}", jwtService.decrypt(ownerAuth.getUsername()), jwtService.decrypt(ownerAuth.getPsswrd()), getIpAddress(request));
            logger.debug("Investigate:",e);
              throw new BadCredentialsException("Bad credentials.");
          }
              Optional<Owner> owner = ownerRepository.findByUsername(jwtService.decrypt(ownerAuth.getUsername()));
              System.out.println("owner: " + owner);
              name = owner.orElseThrow().getName();
              subject = new Subject(ownerAuth.getUsername(), jwtService.encrypt(name.substring(0, name.indexOf(' '))));
              token.setToken(jwtService.generateToken(subject));
              System.out.println(token);
              logger.error("{}:{}", jwtService.decrypt(ownerAuth.getPsswrd()), jwtService.decrypt(ownerAuth.getUsername()));
          refreshToken = tokenRefreshService.createRefreshToken(subject.getUsername());

          return JwtResponse.builder()
                  .accessToken(jwtService.generateToken(subject))
                  .refreshToken(refreshToken.getToken()).build();
    }

    public ResponseMessage ownerLogout(String token) {
        ResponseMessage message = new ResponseMessage();
        System.out.println(token);
        System.out.println(refreshToken);
        refreshTokenRepository.deleteById(refreshToken.getId());
        message.setMessage("Logged out.");
        System.out.println(message.getMessage());
        return message;
    }


    private String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");

        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }

        return ipAddress;
    }


}


