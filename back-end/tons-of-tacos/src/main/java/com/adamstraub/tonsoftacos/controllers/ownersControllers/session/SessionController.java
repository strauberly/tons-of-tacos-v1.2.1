package com.adamstraub.tonsoftacos.controllers.ownersControllers.session;

import com.adamstraub.tonsoftacos.dto.businessDto.ResponseMessage;
import com.adamstraub.tonsoftacos.dto.businessDto.security.*;
import com.adamstraub.tonsoftacos.services.security.AuthService;
import com.adamstraub.tonsoftacos.services.security.TokenRefreshService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class SessionController implements SessionControllerInterface {


    @Autowired
    AuthService authService;
    @Autowired
     TokenRefreshService tokenRefreshService;


    @Override
    public JwtResponse ownerLogin(HttpServletRequest request, OwnerAuth authDto){
        System.out.println("login controller");
        return authService.ownerLogin(request, authDto);
    }

    @Override
    public JwtResponse refreshToken(@CookieValue RefreshToken token) {
        System.out.println("refresh controller: " + token);
        return tokenRefreshService.refreshToken(token);
    }

    @Override
    public ResponseMessage ownerLogout(HttpServletRequest token) {
        System.out.println("logout controller: " + token.getHeader("Authorization"));
       return authService.ownerLogout(token.getHeader("Authorization"))    ;
    }
}

