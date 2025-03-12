package com.adamstraub.tonsoftacos.services.security;
import com.adamstraub.tonsoftacos.dto.businessDto.security.OwnerAuth;
import com.adamstraub.tonsoftacos.dto.businessDto.security.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


//service pertains to authentication functions(login, logout, session timeout etc.)
@Service
@RequiredArgsConstructor
public class AuthService {
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final AuthenticationManager authenticationManager;
    private final Token token = new Token();




    //possibly add logger here for bad login attempts in order to log the submitted credentials separately.
    public Token ownerLogin(OwnerAuth ownerAuth) {
//public String ownerLogin(OwnerAuth ownerAuth) {
//        System.out.println("auth service");
//        System.out.println(ownerAuth);
//
////        try {
//
//            Authentication authentication = authenticationManager
//                    .authenticate(new UsernamePasswordAuthenticationToken(jwtService.decrypt(ownerAuth.getUsername()),
//                            jwtService.decrypt(ownerAuth.getPsswrd())));
////            System.out.println(authentication);
//            if (!authentication.isAuthenticated()) {
////
//                throw new BadCredentialsException("Bad credentials.");
//
//        }
//        return jwtService.generateToken(ownerAuth.getUsername());
//    }

    System.out.println("auth service");
    System.out.println(ownerAuth);
    System.out.println(jwtService.decrypt(ownerAuth.getUsername()));
    System.out.println(jwtService.decrypt(ownerAuth.getPsswrd()));
//        try {

    try{
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(jwtService.decrypt(ownerAuth.getUsername()),
                        jwtService.decrypt(ownerAuth.getPsswrd())));
//        return jwtService.generateToken(ownerAuth.getUsername());
        token.setToken(jwtService.generateToken(ownerAuth.getUsername()));
    } catch (Exception e) {
        throw new BadCredentialsException("Bad credentials.");
    }

    System.out.println(token);
//    return jwtService.generateToken(ownerAuth.getUsername());
    return token;
}
}
