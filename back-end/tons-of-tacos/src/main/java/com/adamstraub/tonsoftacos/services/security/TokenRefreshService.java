package com.adamstraub.tonsoftacos.services.security;

import com.adamstraub.tonsoftacos.dao.OwnerRepository;
import com.adamstraub.tonsoftacos.dao.RefreshTokenRepository;
import com.adamstraub.tonsoftacos.entities.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
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
//                .exp(Instant.now().plusMillis((1000*60) * 10))
                .exp(new Date(System.currentTimeMillis() + (1000 * 60 ) * 10).toInstant())

                .build();
        System.out.println(refreshToken);
        System.out.println(refreshToken.getToken());
        System.out.println(refreshToken.getOwnerInfo());

        return refreshTokenRepository.save(refreshToken);
    }


}
