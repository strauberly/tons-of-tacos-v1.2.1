package com.adamstraub.tonsoftacos.controllers.ownersControllers.session;

import com.adamstraub.tonsoftacos.dto.businessDto.security.*;
import com.adamstraub.tonsoftacos.services.security.AuthService;
import com.adamstraub.tonsoftacos.services.security.JwtService;
import com.adamstraub.tonsoftacos.services.security.TokenRefreshService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class SessionController implements SessionControllerInterface {


    @Autowired
    AuthService authService;

    TokenRefreshService tokenRefreshService;

    JwtService jwtService;

    @Override
    public JwtResponse ownerLogin(OwnerAuth authDto){
        System.out.println("controller");
        return authService.ownerLogin(authDto);
    }

    @Override
    public JwtResponse refreshToken(RefreshToken token) {
        Subject subject = new Subject();

      return  tokenRefreshService.findByToken(token.getRefreshToken())
                .map(TokenRefreshService::verifyExp)
                .map(com.adamstraub.tonsoftacos.entities.RefreshToken::getOwnerInfo)
                .map(owner -> {
                    subject.setUsername(owner.getUsername());
                    subject.setOwnername(owner.getName());
                    String accessToken = jwtService.generateToken(subject);
                    return JwtResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(token.getRefreshToken())
                            .build();
                }).orElseThrow(() -> new RuntimeException("Invalid refresh"));
    }
    }

//    @Override
//    public Token ownerLogin(OwnerAuth authDto){
//        System.out.println("controller");
//        return authService.ownerLogin(authDto);
//    }


//    @Override
//    public Token ownerLogin(OwnerAuth authDto){
//        System.out.println("controller");
//        return authService.ownerLogin(authDto);
//    }

//    @Override
//    public String ownerLogin(OwnerAuth authDto){
//        System.out.println("controller");
//        return authService.ownerLogin(authDto);
//    }

