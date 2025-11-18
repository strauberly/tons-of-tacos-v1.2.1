package com.adamstraub.tonsoftacos.services.security;
import com.adamstraub.tonsoftacos.dao.OwnerRepository;
import com.adamstraub.tonsoftacos.dto.businessDto.security.JwtResponse;
import com.adamstraub.tonsoftacos.dto.businessDto.security.OwnerAuth;
import com.adamstraub.tonsoftacos.dto.businessDto.security.Subject;
import com.adamstraub.tonsoftacos.dto.businessDto.security.Token;
import com.adamstraub.tonsoftacos.entities.Owner;
import com.adamstraub.tonsoftacos.entities.RefreshToken;
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
    private final TokenRefreshService tokenRefreshService;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final OwnerRepository ownerRepository;
    private final Token token = new Token();
    private  final JwtResponse jwtResponse = new JwtResponse();

    RefreshToken refreshToken;


    //possibly add logger here for bad login attempts in order to log the submitted credentials separately.
//    public Token ownerLogin(OwnerAuth ownerAuth) {
//        public Token ownerLogin(OwnerAuth ownerAuth) {
    public JwtResponse ownerLogin(OwnerAuth ownerAuth) {

        String name;
        System.out.println("auth service");
        System.out.println(ownerAuth);
        System.out.println(jwtService.decrypt(ownerAuth.getUsername()));
        System.out.println(jwtService.decrypt(ownerAuth.getPsswrd()));
//        RefreshToken refreshToken;
        Subject subject;
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(jwtService.decrypt(ownerAuth.getUsername()),
                            jwtService.decrypt(ownerAuth.getPsswrd())));
            Optional<Owner> owner = ownerRepository.findByUsername(jwtService.decrypt(ownerAuth.getUsername()));
            System.out.println("owner: " + owner);
            name = owner.orElseThrow().getName();

            subject = new Subject(ownerAuth.getUsername(), jwtService.encrypt(name.substring(0, name.indexOf(' '))));
//        Subject subject = new Subject(jwtService.decrypt(ownerAuth.getUsername()), name.substring(0, name.indexOf(' ')));
            token.setToken(jwtService.generateToken(subject));
//            refreshToken = TokenRefreshService.createRefreshToken(ownerAuth.getUsername());
//            JwtResponse.builder()
//                    .accessToken(jwtService.generateToken(subject))
//                    .token(refreshToken.getToken()).build();
//        token.setToken(jwtService.generateToken(jwtService.encrypt(name.substring(0, name.indexOf(' ')))));
            System.out.println(token);

        } catch (Exception e) {
            throw new BadCredentialsException("Bad credentials.");
        }

//    System.out.println(response);

//            return token;
//         RefreshToken refreshToken = TokenRefreshService.createRefreshToken(ownerAuth.getUsername());
        refreshToken = tokenRefreshService.createRefreshToken(subject.getUsername());
        return JwtResponse.builder()
                .accessToken(jwtService.generateToken(subject))
                .refreshToken(refreshToken.getToken()).build();


    }

}


