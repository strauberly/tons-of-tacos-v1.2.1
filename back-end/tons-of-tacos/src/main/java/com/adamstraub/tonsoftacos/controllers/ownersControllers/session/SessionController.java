package com.adamstraub.tonsoftacos.controllers.ownersControllers.session;

import com.adamstraub.tonsoftacos.dto.businessDto.ResponseMessageDTO;
import com.adamstraub.tonsoftacos.dto.securityDto.JwtResponseDTO;
import com.adamstraub.tonsoftacos.dto.securityDto.OwnerAuthDTO;
import com.adamstraub.tonsoftacos.dto.securityDto.ResfreshTokenDTO;
import com.adamstraub.tonsoftacos.services.security.AuthService;
import com.adamstraub.tonsoftacos.services.security.TokenRefreshService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RestController;




@RestController
public class SessionController implements SessionControllerInterface {


    @Autowired
    private AuthService authService;
    @Autowired
     private TokenRefreshService tokenRefreshService;



    @Override
    public ResponseEntity<JwtResponseDTO> ownerLogin(HttpServletRequest request, OwnerAuthDTO authDto){
        System.out.println("login controller");
        return authService.ownerLogin(request, authDto);
    }

    @Override
    public ResponseEntity<JwtResponseDTO> refreshToken(@CookieValue ResfreshTokenDTO token) {
        System.out.println("refresh controller: " + token);
        return tokenRefreshService.refreshToken(token);
    }

    @Override
    public ResponseEntity<ResponseMessageDTO> ownerLogout(HttpServletRequest request, @CookieValue ResfreshTokenDTO token) {
        System.out.println(token);
        return authService.ownerLogout(request, token);
    }

}

