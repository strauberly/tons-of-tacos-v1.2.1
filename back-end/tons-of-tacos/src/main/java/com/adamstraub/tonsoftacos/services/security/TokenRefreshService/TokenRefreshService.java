package com.adamstraub.tonsoftacos.services.security.TokenRefreshService;

import com.adamstraub.tonsoftacos.dto.securityDto.SubjectDTO;
import com.adamstraub.tonsoftacos.entities.RefreshToken;
import com.adamstraub.tonsoftacos.repository.OwnerRepository;
import com.adamstraub.tonsoftacos.repository.RefreshTokenRepository;
import com.adamstraub.tonsoftacos.dto.securityDto.JwtResponseDTO;
import com.adamstraub.tonsoftacos.dto.securityDto.RefreshTokenDTO;
//import com.adamstraub.tonsoftacos.dto.securityDto.SubjectDTO;
import com.adamstraub.tonsoftacos.entities.Owner;
import com.adamstraub.tonsoftacos.services.security.EncryptionService.IEncryptionService;
import com.adamstraub.tonsoftacos.services.security.JwtService.IJwtService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public  class TokenRefreshService implements ITokenRefreshService{
@Autowired
    private final RefreshTokenRepository refreshTokenRepository;
@Autowired
    private final OwnerRepository ownerRepository;
@Autowired
    private final IJwtService jwtService;
@Autowired
    private IEncryptionService encryptionService;

    @Override
    public RefreshToken createRefreshToken(String userName){
        int id = 0;
        Owner owner = ownerRepository.findByUsername(encryptionService.decrypt(userName))
                .orElseThrow(()-> new EntityNotFoundException("No owner found for user name: " + userName));
        int ownerID = owner.getOwnerId();
        try {
            List<com.adamstraub.tonsoftacos.entities.RefreshToken> oldTokenlist = refreshTokenRepository.findAll();
            for(RefreshToken oldToken : oldTokenlist) {
                if (oldToken.getOwnerInfo().getOwnerId() == ownerID) {
                    id = oldToken.getId();
                    log.info("Previous token for user found during refresh: {}", oldToken);
                }
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

        try {
            RefreshToken refreshToken = RefreshToken.builder()
                    .id(id)
                    .ownerInfo(ownerRepository.findByUsername(encryptionService.decrypt(userName))
                            .orElseThrow(()-> new EntityNotFoundException("No owner found for user name: " + userName)))
                    .token(UUID.randomUUID().toString())
                    .exp(Date.from(Instant.now().plusMillis((1000*60) * 4)))
                    .build();
            log.info("refreshToken to be returned: {}", refreshToken);
            refreshTokenRepository.save(refreshToken);
            return refreshToken;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    public RefreshToken findByToken(String token){
        try {
            return refreshTokenRepository.findByToken(token);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("No token found: " + e);
        }

    }

    @Transactional
    public RefreshToken verifyExpiration(RefreshToken refreshToken){
        if (refreshToken.getExp().compareTo(new Date(System.currentTimeMillis()))<0){
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException(refreshToken.getToken() + "Refresh expired try again");
        }
        return refreshToken;
    }

    @Transactional
    @Override
    public ResponseEntity<JwtResponseDTO> refreshToken(RefreshTokenDTO token) {
        log.info("Refreshing refresh token:  {}", token);
//        RefreshToken oldToken = verifyExpiration(findByToken(token.getRefreshToken()));
        RefreshToken oldToken = verifyExpiration(findByToken(jwtService.extractRefreshToken(token.getRefreshToken())));
        String ownerName = oldToken.getOwnerInfo().getName();
        String userName = oldToken.getOwnerInfo().getUsername();
        SubjectDTO subject;
//        subject = new SubjectDTO(encryptionService.encrypt(userName),
//                encryptionService.encrypt(ownerName.substring(0, ownerName.indexOf(' '))));
        RefreshToken newRefreshToken = createRefreshToken(encryptionService.encrypt(userName));
//        double check if encryption needed or what is going on
        subject = new SubjectDTO(encryptionService.encrypt(userName),
                encryptionService.encrypt(ownerName.substring(0, ownerName.indexOf(' '))),
                newRefreshToken.getToken());
//        RefreshToken newRefreshToken = createRefreshToken(subject.getUsername());
        log.info("new refresh token:  {}", newRefreshToken.getToken());
//        jwt response to be reworked or eliminated.
        JwtResponseDTO response = JwtResponseDTO.builder()
                .accessToken(jwtService.generateToken(subject))
                .refreshToken(newRefreshToken.getToken()).build();
                try {
                    return ResponseEntity.ok(response);
                } catch (RuntimeException e) {
                    throw new RuntimeException(e);
                }
    }
}
