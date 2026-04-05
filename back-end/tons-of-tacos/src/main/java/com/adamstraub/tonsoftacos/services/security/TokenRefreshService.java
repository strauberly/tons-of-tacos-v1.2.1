package com.adamstraub.tonsoftacos.services.security;

import com.adamstraub.tonsoftacos.respository.OwnerRepository;
import com.adamstraub.tonsoftacos.respository.RefreshTokenRepository;
import com.adamstraub.tonsoftacos.dto.businessDto.security.JwtResponse;
import com.adamstraub.tonsoftacos.dto.businessDto.security.RefreshToken;
import com.adamstraub.tonsoftacos.dto.businessDto.security.Subject;
import com.adamstraub.tonsoftacos.entities.Owner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public  class TokenRefreshService {
@Autowired
    private final RefreshTokenRepository refreshTokenRepository;
@Autowired
    private final OwnerRepository ownerRepository;
@Autowired
    private final JwtService jwtService;


    public com.adamstraub.tonsoftacos.entities.RefreshToken createRefreshToken(String userName){
        Owner owner = ownerRepository.findByUsername(jwtService.decrypt(userName)).get();

        int ownerID = owner.getOwnerId();
        List<com.adamstraub.tonsoftacos.entities.RefreshToken> oldTokenlist = refreshTokenRepository.findAll();
        for(com.adamstraub.tonsoftacos.entities.RefreshToken oldToken : oldTokenlist){
            if(oldToken.getOwnerInfo().getOwnerId() == ownerID){
                log.info("token for user found and deleted: {}", oldToken);
                refreshTokenRepository.deleteById(oldToken.getId());
            }
        }

//        import refresh token entity
        com.adamstraub.tonsoftacos.entities.RefreshToken refreshToken =
        com.adamstraub.tonsoftacos.entities.RefreshToken.builder()
                .ownerInfo(ownerRepository.findByUsername(jwtService.decrypt(userName)).get())
                .token(UUID.randomUUID().toString())
                .exp(Date.from(Instant.now().plusMillis((1000*60) * 4)))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public com.adamstraub.tonsoftacos.entities.RefreshToken findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public com.adamstraub.tonsoftacos.entities.RefreshToken verifyExp(com.adamstraub.tonsoftacos.entities.RefreshToken refreshToken){
        if (refreshToken.getExp().compareTo(new Date(System.currentTimeMillis()))<0){
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException(refreshToken.getToken() + "Refresh expired try again");
        }
        return refreshToken;
    }


    public ResponseEntity<JwtResponse> refreshToken(RefreshToken token) {
        com.adamstraub.tonsoftacos.entities.RefreshToken oldToken = verifyExp(findByToken(token.getRefreshToken()));
        String name = oldToken.getOwnerInfo().getName();
        Subject subject = new Subject();
        String uuid = UUID.randomUUID().toString();

        subject.setOwnername(name.substring(0, name.indexOf(' ')));
        subject.setUsername(jwtService.encrypt(oldToken.getOwnerInfo().getUsername()));
        String accessToken = jwtService.generateToken(subject);

        oldToken.setToken(uuid);
                oldToken.setExp(new Date((System.currentTimeMillis() + (1000 * 60) * 4)));

        com.adamstraub.tonsoftacos.entities.RefreshToken refreshToken =
        com.adamstraub.tonsoftacos.entities.RefreshToken.builder()
                .ownerInfo(oldToken.getOwnerInfo())
                .token(uuid)
                .exp(Date.from(Instant.now().plusMillis((1000*60) * 4)))
                .build();
        System.out.println(refreshToken);
        System.out.println(refreshToken.getToken());
        System.out.println(refreshToken.getOwnerInfo());
        refreshTokenRepository.save(oldToken);


        return ResponseEntity.ok(JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(uuid)
                .build());
    }

}
