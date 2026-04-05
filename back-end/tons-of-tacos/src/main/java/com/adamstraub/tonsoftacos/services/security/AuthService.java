package com.adamstraub.tonsoftacos.services.security;
import com.adamstraub.tonsoftacos.respository.OwnerRepository;
import com.adamstraub.tonsoftacos.respository.RefreshTokenRepository;
import com.adamstraub.tonsoftacos.dto.businessDto.ResponseMessage;
import com.adamstraub.tonsoftacos.dto.businessDto.security.*;
import com.adamstraub.tonsoftacos.entities.Owner;
import com.adamstraub.tonsoftacos.entities.RefreshToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final TokenRefreshService tokenRefreshService;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final OwnerRepository ownerRepository;
    @Autowired
    private final RefreshTokenRepository refreshTokenRepository;



    private final Token token = new Token();


    private String owner;


    public ResponseEntity<JwtResponse> ownerLogin(@NotNull HttpServletRequest request, OwnerAuth ownerAuth) {
        System.out.println("auth service");
        owner = jwtService.decrypt(ownerAuth.getUsername());
        String name;
        Subject subject;
        try{
           authenticationManager
                  .authenticate(new UsernamePasswordAuthenticationToken(jwtService.decrypt(ownerAuth.getUsername()),
                          jwtService.decrypt(ownerAuth.getPsswrd())));
          } catch (Exception e) {
            log.error("Credentials submitted:\n user: {}, password: {}\n from address: {}", jwtService.decrypt(ownerAuth.getUsername()), jwtService.decrypt(ownerAuth.getPsswrd()), getIpAddress(request));
            log.debug("Investigate:",e);
              throw new BadCredentialsException("Bad credentials.", e);
          }
//seperate try catch with runtime exception/system failure database down
        Optional<Owner> owner = ownerRepository.findByUsername(jwtService.decrypt(ownerAuth.getUsername()));
              name = owner.orElseThrow().getName();
              subject = new Subject(ownerAuth.getUsername(), jwtService.encrypt(name.substring(0, name.indexOf(' '))));
              token.setToken(jwtService.generateToken(subject));
              log.info("Successful Login: \n user: {}, location:{}", jwtService.decrypt(ownerAuth.getUsername()), getIpAddress(request) );
        RefreshToken refreshToken = tokenRefreshService.createRefreshToken(subject.getUsername());
        return ResponseEntity.ok(JwtResponse.builder()
                .accessToken(jwtService.generateToken(subject))
                .refreshToken(refreshToken.getToken()).build());
    }


public ResponseEntity<ResponseMessage> ownerLogout(@NotNull HttpServletRequest request, com.adamstraub.tonsoftacos.dto.businessDto.security.RefreshToken token) {
        System.out.println("auth service");
        ResponseMessage message = new ResponseMessage();
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

//    public void endOfDay() {
//
////        date
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        String today = df.format(new Date());
//        refreshTokenRepository.deleteAll();
//        emailService.endOfDayEmailDevTeam("superduper.devteam@manyme.com", "End of day report: " + today);
//
//    }


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


