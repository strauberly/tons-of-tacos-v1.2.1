package com.adamstraub.tonsoftacos.services.security;

import com.adamstraub.tonsoftacos.dao.OwnerRepository;
import com.adamstraub.tonsoftacos.dao.RefreshTokenRepository;
import com.adamstraub.tonsoftacos.dto.businessDto.security.JwtResponse;
import com.adamstraub.tonsoftacos.dto.businessDto.security.RefreshTokenReq;
import com.adamstraub.tonsoftacos.dto.businessDto.security.Subject;
import com.adamstraub.tonsoftacos.entities.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public  class TokenRefreshService {
    @Autowired
    private final RefreshTokenRepository refreshTokenRepository;
@Autowired
    private final OwnerRepository ownerRepository;

    @Autowired
    private final JwtService jwtService;



    public  RefreshToken createRefreshToken(String userName){
        System.out.println("username 2: " + jwtService.decrypt(userName));
        System.out.println("username 3: " + ownerRepository.findByUsername(jwtService.decrypt(userName)).get());
        RefreshToken refreshToken =
        RefreshToken.builder()
                .ownerInfo(ownerRepository.findByUsername(jwtService.decrypt(userName)).get())
                .token(UUID.randomUUID().toString())
                .exp(Date.from(Instant.now().plusMillis((1000*60) * 4)))
//                .exp(new Date((System.currentTimeMillis() + (1000 * 60 * 60) * 4)))
                .build();
        System.out.println(refreshToken);
        System.out.println(refreshToken.getToken());
        System.out.println(refreshToken.getOwnerInfo());
  
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public  RefreshToken verifyExp(RefreshToken refreshToken){
//        if (refreshToken.getExp().compareTo(Instant.now())<0){
        if (refreshToken.getExp().compareTo(new Date(System.currentTimeMillis()))<0){
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException(refreshToken.getToken() + "Refresh expired try again");
        }
        return refreshToken;
    }


    public JwtResponse refreshToken(RefreshTokenReq token) {

        System.out.println("refresh service : token: " + token);
        RefreshToken oldToken =   verifyExp(findByToken(token.getRefreshToken()));
        String name = oldToken.getOwnerInfo().getName();
                System.out.println("old token: " + oldToken);
        Subject subject = new Subject();
        String uuid = UUID.randomUUID().toString();
        subject.setOwnername(name.substring(0, name.indexOf(' ')));
        subject.setUsername(jwtService.encrypt(oldToken.getOwnerInfo().getUsername()));
        System.out.println("subject: " + subject);
        String accessToken = jwtService.generateToken(subject);
        System.out.println("access token: " + accessToken);
//        JwtResponse response = JwtResponse.builder()
//                .accessToken(accessToken)
//                .refreshToken(UUID.randomUUID().toString())
//                .build();
//        System.out.println("response: " + response);
        oldToken.setToken(uuid);
                oldToken.setExp(new Date((System.currentTimeMillis() + (1000 * 60) * 4)));
//        oldToken.setExp(new Date((System.currentTimeMillis() + (1000 * 60 * 60 ) * 4)));

        RefreshToken refreshToken =
        RefreshToken.builder()
                .ownerInfo(oldToken.getOwnerInfo())
                .token(uuid)
                .exp(Date.from(Instant.now().plusMillis((1000*60) * 4)))
//                .exp(new Date((System.currentTimeMillis() + (1000 * 60 * 60 ) * 4)))
                .build();
        System.out.println(refreshToken);
        System.out.println(refreshToken.getToken());
        System.out.println(refreshToken.getOwnerInfo());
//        refreshTokenRepository.deleteAll();
//        refreshTokenRepository.deleteById(oldToken.getId());
//         refreshTokenRepository.save(refreshToken);
        refreshTokenRepository.save(oldToken);
//        next step is deleting previous refresh token
//         refreshTokenRepository.
        System.out.println("saved Token: " + refreshTokenRepository.findByToken(refreshToken.getToken()).getToken());

        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(uuid)
                .build();
    }

}
