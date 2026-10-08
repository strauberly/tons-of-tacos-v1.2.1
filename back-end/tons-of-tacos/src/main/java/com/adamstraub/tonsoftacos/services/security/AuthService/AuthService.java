package com.adamstraub.tonsoftacos.services.security.AuthService;
import com.adamstraub.tonsoftacos.dto.securityDto.*;
import com.adamstraub.tonsoftacos.repository.OwnerRepository;
import com.adamstraub.tonsoftacos.repository.RefreshTokenRepository;
import com.adamstraub.tonsoftacos.dto.businessDto.ResponseMessageDTO;
import com.adamstraub.tonsoftacos.entities.Owner;
import com.adamstraub.tonsoftacos.entities.RefreshToken;
import com.adamstraub.tonsoftacos.services.security.EncryptionService.IEncryptionService;
import com.adamstraub.tonsoftacos.services.security.JwtService.IJwtService;
import com.adamstraub.tonsoftacos.services.security.TokenRefreshService.ITokenRefreshService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.Cookie;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService{
    @Autowired
    private final IJwtService jwtService;
    @Autowired
    private IEncryptionService encryptionService;
    @Autowired
    private final ITokenRefreshService tokenRefreshService;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final OwnerRepository ownerRepository;
    @Autowired
    private final RefreshTokenRepository refreshTokenRepository;

    private String owner;

//    public ResponseEntity<JwtResponseDTO> ownerLogin(@NotNull HttpServletRequest request, OwnerAuthDTO ownerAuth) {
public ResponseEntity<JwtResponseDTO> ownerLogin(@NotNull HttpServletResponse response, @NotNull HttpServletRequest request, OwnerAuthDTO ownerAuth) {
    owner = encryptionService.decrypt(ownerAuth.getUsername());
        String name = "";
        SubjectDTO subject;
        Authentication auth;
//    HttpServletResponse response = null;

//    response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
//    response.setHeader("Access-Control-Allow-Credentials", "true");
//    response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
//    response.setHeader("Access-Control-Allow-Headers", "Content-Type");
//
//    // Set cookie
//    Cookie cookie = new Cookie("SESSION", "test-session-value");
//    cookie.setHttpOnly(true);
//    cookie.setSecure(false); // Change to true if using HTTPS
//    cookie.setPath("/owners-tools");
//    cookie.setMaxAge(3600);
//    response.addCookie(cookie);
//    1) http://localhost:8080/api/owners-tools/login, ORIGIN=http://localhost:3000, 2)
        try{
           auth = authenticationManager
                  .authenticate(new UsernamePasswordAuthenticationToken(encryptionService.decrypt(ownerAuth.getUsername()),
                          encryptionService.decrypt(ownerAuth.getPsswrd())));
          } catch (Exception e) {
            log.error("Credentials submitted:\n user: {}, password: {}\n from address: {}",
                    encryptionService.decrypt(ownerAuth.getUsername()),
                    encryptionService.decrypt(ownerAuth.getPsswrd()),
                    getIpAddress(request));
            log.debug("Investigate above line 62: ", e);
              throw new BadCredentialsException("Bad credentials.", e);
          }
        try {
            if(auth.isAuthenticated()){
                Optional<Owner> owner = ownerRepository.findByUsername(encryptionService.decrypt(ownerAuth.getUsername()));
                name = owner.orElseThrow().getName();
            }
        } catch (RuntimeException e) {
            throw new RuntimeException( e);
        }
    RefreshToken refreshToken = tokenRefreshService.createRefreshToken(ownerAuth.getUsername());
//        subject = new SubjectDTO(ownerAuth.getUsername(), encryptionService.encrypt(name.substring(0, name.indexOf(' ')), refreshToken));
    log.info("userName: {}", ownerAuth.getUsername());
    subject = new SubjectDTO(ownerAuth.getUsername(), encryptionService.encrypt(name.substring(0, name.indexOf(' '))), refreshToken.getToken());

//        RefreshToken refreshToken = tokenRefreshService.createRefreshToken(subject.getUsername());
//        JwtResponseDTO response = JwtResponseDTO.builder()
    JwtResponseDTO jwtResponse = JwtResponseDTO.builder()
                 .accessToken(jwtService.generateToken(subject))
                .refreshToken(refreshToken.getToken()).build();
// cookie addition
//    front end should be checking cookie name
        String cookieName = ownerAuth.getUsername();
//        Cookie cookie = new Cookie("access", jwtService.generateToken(subject));
    log.info("cookie name: {}", cookieName);
//    Cookie cookie = new Cookie(cookieName, jwtService.generateToken(subject));
//    cookie.setHttpOnly(true);
//        cookie.setSecure(true);
//        cookie.setPath("/owners-tools");
//        cookie.setMaxAge(3600);
        ResponseCookie rc = ResponseCookie.from(cookieName, jwtService.generateToken(subject))
                .httpOnly(true)
                .secure(true)
                .path("/owners-tools")
                .maxAge(3600)
                .sameSite("None")
                .build();
//    cookie.setPath("/owners-tools");
//        cookie.
        log.info("cookie created: {}", rc.getName());
    log.info("cookie created: {}", rc.getValue());
//        response.addCookie(cookie);
//        set custom header
    response.addHeader("Set-Cookie", rc.toString());
//    response.setHeader("","");
//    HttpServletResponse response = (HttpServletResponse) request.getServletContext();
//    response.addCookie(cookie);
//    log.info("cookie {}", cookie.getValue());
    log.info("Cookie name: {}, path: {}, httpOnly: {}, maxAge: {}",
//            cookie.getName(), cookie.getPath(), cookie.isHttpOnly(), cookie.getMaxAge());
            rc.getName(), rc.getPath(), rc.isHttpOnly(), rc.getMaxAge());
        log.info("Successful Login: \n user: {}, location:{}", encryptionService.decrypt(ownerAuth.getUsername()), getIpAddress(request));
        return ResponseEntity.ok(jwtResponse);
//    return ResponseEntity.ok(response);
    }

public ResponseEntity<ResponseMessageDTO> ownerLogout(@NotNull HttpServletRequest request, RefreshTokenDTO token) {
        ResponseMessageDTO message = new ResponseMessageDTO();
        RefreshToken rftoken = refreshTokenRepository.findByToken(token.getRefreshToken());
        try {
        refreshTokenRepository.delete(rftoken);
        message.setMessage("Logged out.");
        log.info("Successful Logout: \n user: {}, location:{}", owner, getIpAddress(request));
        } catch (Exception e) {
            log.error("Error logging out: {}, from address: {}", owner, getIpAddress(request));
            log.debug("Investigate:",e);
        }
        return ResponseEntity.ok(message);
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
            log.error("Error getting IP address.");
            log.debug("Investigate:",e);
        }
        return ipAddress;
    }
}


