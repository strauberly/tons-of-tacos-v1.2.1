package com.adamstraub.tonsoftacos.controllers.ownersControllers.session;

import com.adamstraub.tonsoftacos.dto.businessDto.ResponseMessage;
import com.adamstraub.tonsoftacos.dto.businessDto.security.*;
import com.adamstraub.tonsoftacos.services.security.AuthService;
import com.adamstraub.tonsoftacos.services.security.JwtService;
import com.adamstraub.tonsoftacos.services.security.TokenRefreshService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class SessionController implements SessionControllerInterface {


    @Autowired
    AuthService authService;
    @Autowired
     TokenRefreshService tokenRefreshService;
    @Autowired
    JwtService jwtService;

    @Override
    public JwtResponse ownerLogin(OwnerAuth authDto){
        System.out.println("controller");
        return authService.ownerLogin(authDto);
    }

    @Override
    public JwtResponse refreshToken(@CookieValue RefreshTokenReq token) {
        System.out.println("refresh controller: " + token);
        return tokenRefreshService.refreshToken(token);
    }



    @Override
    public ResponseMessage ownerLogout(String token) {
        System.out.println("logout controller: " + token);
       return authService.ownerLogout(token)    ;
    }
//        Subject subject = new Subject();
}
//      return  tokenRefreshService.findByToken(token.getRefreshToken())
//                .map(TokenRefreshService::verifyExp)
//                .map(com.adamstraub.tonsoftacos.entities.RefreshToken::getOwnerInfo)
//                .map(owner -> {
//                    subject.setUsername(owner.getUsername());
//                    subject.setOwnername(owner.getName());
//                    String accessToken = jwtService.generateToken(subject);
//                    return JwtResponse.builder()
//                            .accessToken(accessToken)
//                            .refreshToken(token.getRefreshToken())
//                            .build();
//                }).orElseThrow(() -> new RuntimeException("Invalid refresh"));
//    }
//    }

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

