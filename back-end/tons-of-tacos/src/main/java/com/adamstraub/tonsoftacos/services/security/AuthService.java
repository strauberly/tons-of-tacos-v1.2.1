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
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    private String owner;
    public JwtResponse ownerLogin(@NotNull HttpServletRequest request, OwnerAuth ownerAuth) {
        owner = jwtService.decrypt(ownerAuth.getUsername());
        String name;
        System.out.println("auth service");


        Subject subject;
        try{
           authenticationManager
                  .authenticate(new UsernamePasswordAuthenticationToken(jwtService.decrypt(ownerAuth.getUsername()),
                          jwtService.decrypt(ownerAuth.getPsswrd())));
          } catch (Exception e) {
              logger.error("Credentials submitted:\n user: {}, password: {}\n from address: {}", jwtService.decrypt(ownerAuth.getUsername()), jwtService.decrypt(ownerAuth.getPsswrd()), getIpAddress(request));
            logger.debug("Investigate:",e);
              throw new BadCredentialsException("Bad credentials.");
          }
              Optional<Owner> owner = ownerRepository.findByUsername(jwtService.decrypt(ownerAuth.getUsername()));
              System.out.println("owner: " + owner);
              name = owner.orElseThrow().getName();
              subject = new Subject(ownerAuth.getUsername(), jwtService.encrypt(name.substring(0, name.indexOf(' '))));
              token.setToken(jwtService.generateToken(subject));
              System.out.println(token);
              logger.info("Successful Login: \n user: {}, location:{}", jwtService.decrypt(ownerAuth.getUsername()), getIpAddress(request) );
          refreshToken = tokenRefreshService.createRefreshToken(subject.getUsername());

          return JwtResponse.builder()
                  .accessToken(jwtService.generateToken(subject))
                  .refreshToken(refreshToken.getToken()).build();
    }

    public ResponseMessage ownerLogout(@NotNull HttpServletRequest request, String token) {
        System.out.println("auth service");
        ResponseMessage message = new ResponseMessage();
        try {


        refreshTokenRepository.deleteById(refreshToken.getId());
        message.setMessage("Owner Logged out.");
        logger.info("Successful Logout: \n user: {}, location:{}", owner, getIpAddress(request));
        } catch (Exception e) {
            logger.error("Error logging out: {}, from address: {}", owner, getIpAddress(request));
            logger.debug("Investigate:",e);
        }
        return message;
    }


    private String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        try {
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
            }
        } catch (Exception e) {
            logger.error("Error getting IP address.");
            logger.debug("Investigate:",e);
        }


        return ipAddress;
    }


}


