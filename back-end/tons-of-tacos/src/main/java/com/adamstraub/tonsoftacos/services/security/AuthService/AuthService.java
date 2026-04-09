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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

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



    private final TokenDTO tokenDTO = new TokenDTO();
    private String owner;


    public ResponseEntity<JwtResponseDTO> ownerLogin(@NotNull HttpServletRequest request, OwnerAuthDTO ownerAuth) {
        owner = encryptionService.decrypt(ownerAuth.getUsername());
        String name = "";
        SubjectDTO subject;
        Authentication auth;
        try{

           auth = authenticationManager
                  .authenticate(new UsernamePasswordAuthenticationToken(encryptionService.decrypt(ownerAuth.getUsername()),
                          encryptionService.decrypt(ownerAuth.getPsswrd())));
          } catch (Exception e) {
            log.error("Credentials submitted:\n user: {}, password: {}\n from address: {}",
                    encryptionService.decrypt(ownerAuth.getUsername()),
                    encryptionService.decrypt(ownerAuth.getPsswrd()),
                    getIpAddress(request));
            log.debug("Investigate:",e);
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
        subject = new SubjectDTO(ownerAuth.getUsername(), encryptionService.encrypt(name.substring(0, name.indexOf(' '))));
              tokenDTO.setToken(jwtService.generateToken(subject));
              log.info("Successful Login: \n user: {}, location:{}", encryptionService.decrypt(ownerAuth.getUsername()), getIpAddress(request) );
        RefreshToken refreshToken = tokenRefreshService.createRefreshToken(subject.getUsername());
        return ResponseEntity.ok(JwtResponseDTO.builder()
                .accessToken(jwtService.generateToken(subject))
                .refreshToken(refreshToken.getToken()).build());
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


