package com.adamstraub.tonsoftacos.services.security;
import com.adamstraub.tonsoftacos.dao.OwnerRepository;
import com.adamstraub.tonsoftacos.dto.businessDto.security.OwnerAuth;
import com.adamstraub.tonsoftacos.dto.businessDto.security.Subject;
import com.adamstraub.tonsoftacos.dto.businessDto.security.Token;
import com.adamstraub.tonsoftacos.entities.Owner;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;


//service pertains to authentication functions(login, logout, session timeout etc.)
@Service
@RequiredArgsConstructor
public class AuthService {
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final OwnerRepository ownerRepository;
    private final Token token = new Token();
   


    //possibly add logger here for bad login attempts in order to log the submitted credentials separately.
//    public Token ownerLogin(OwnerAuth ownerAuth) {
        public Token ownerLogin(OwnerAuth ownerAuth) {
        String name;
    System.out.println("auth service");
    System.out.println(ownerAuth);
    System.out.println(jwtService.decrypt(ownerAuth.getUsername()));
    System.out.println(jwtService.decrypt(ownerAuth.getPsswrd()));
    try{
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(jwtService.decrypt(ownerAuth.getUsername()),
                        jwtService.decrypt(ownerAuth.getPsswrd())));
        Optional<Owner> owner = ownerRepository.findByUsername(jwtService.decrypt(ownerAuth.getUsername()));
        System.out.println(owner);
        name = owner.orElseThrow().getName();

        Subject subject = new Subject(ownerAuth.getUsername(), jwtService.encrypt(name.substring(0, name.indexOf(' '))));
//        Subject subject = new Subject(jwtService.decrypt(ownerAuth.getUsername()), name.substring(0, name.indexOf(' ')));
        token.setToken(jwtService.generateToken(subject));
//        token.setToken(jwtService.generateToken(jwtService.encrypt(name.substring(0, name.indexOf(' ')))));
        System.out.println(token);

    } catch (Exception e) {
        throw new BadCredentialsException("Bad credentials.");
    }

//    System.out.println(response);

            return token;
}

}


