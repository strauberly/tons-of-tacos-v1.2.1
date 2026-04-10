package com.adamstraub.tonsoftacos.services.security.TokenRefreshService;

import com.adamstraub.tonsoftacos.entities.RefreshToken;
import com.adamstraub.tonsoftacos.repository.OwnerRepository;
import com.adamstraub.tonsoftacos.repository.RefreshTokenRepository;
import com.adamstraub.tonsoftacos.dto.securityDto.JwtResponseDTO;
import com.adamstraub.tonsoftacos.dto.securityDto.RefreshTokenDTO;
import com.adamstraub.tonsoftacos.dto.securityDto.SubjectDTO;
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
        Owner owner = ownerRepository.findByUsername(encryptionService.decrypt(userName))
                .orElseThrow(()-> new EntityNotFoundException("No owner found for user name: " + userName));
        int ownerID = owner.getOwnerId();
        try {
            List<com.adamstraub.tonsoftacos.entities.RefreshToken> oldTokenlist = refreshTokenRepository.findAll();
            for(com.adamstraub.tonsoftacos.entities.RefreshToken oldToken : oldTokenlist) {
                if (oldToken.getOwnerInfo().getOwnerId() == ownerID) {
                    log.info("token for user found and deleted: {}", oldToken);
                    refreshTokenRepository.deleteById(oldToken.getId());
                }
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

        try {
            RefreshToken refreshToken = RefreshToken.builder()
                    .ownerInfo(ownerRepository.findByUsername(encryptionService.decrypt(userName))
                            .orElseThrow(()-> new EntityNotFoundException("No owner found for user name: " + userName)))
                    .token(UUID.randomUUID().toString())
                    .exp(Date.from(Instant.now().plusMillis((1000*60) * 4)))
                    .build();
            return refreshTokenRepository.save(refreshToken);
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
        RefreshToken oldToken = verifyExpiration(findByToken(token.getRefreshToken()));
        String name = oldToken.getOwnerInfo().getName();
        SubjectDTO subject = new SubjectDTO();
        String uuid = UUID.randomUUID().toString();

        subject.setOwnername(name.substring(0, name.indexOf(' ')));
        subject.setUsername(encryptionService.encrypt(oldToken.getOwnerInfo().getUsername()));
        String accessToken = jwtService.generateToken(subject);

        oldToken.setToken(uuid);
        oldToken.setExp(new Date((System.currentTimeMillis() + (1000 * 60) * 4)));
                try {
                    refreshTokenRepository.save(oldToken);
                    return ResponseEntity.ok(JwtResponseDTO.builder()
                            .accessToken(accessToken)
                            .refreshToken(uuid)
                            .build());
                } catch (RuntimeException e) {
                    throw new RuntimeException(e);
                }
    }
}
