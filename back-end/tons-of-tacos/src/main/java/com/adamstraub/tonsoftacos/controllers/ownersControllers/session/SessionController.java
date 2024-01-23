package com.adamstraub.tonsoftacos.controllers.ownersControllers.session;

import com.adamstraub.tonsoftacos.dto.businessDto.security.OwnerAuth;
import com.adamstraub.tonsoftacos.services.security.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class SessionController implements SessionControllerInterface {


    @Autowired
    AuthService authService;

    @Override
    public String ownerLogin(OwnerAuth authDto){
        System.out.println("controller");
        return authService.ownerLogin(authDto);
    }
}
