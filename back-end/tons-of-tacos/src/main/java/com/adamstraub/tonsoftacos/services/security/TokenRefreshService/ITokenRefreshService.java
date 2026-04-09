package com.adamstraub.tonsoftacos.services.security.TokenRefreshService;

import com.adamstraub.tonsoftacos.dto.securityDto.JwtResponseDTO;
import com.adamstraub.tonsoftacos.dto.securityDto.RefreshTokenDTO;
import com.adamstraub.tonsoftacos.entities.RefreshToken;
import org.springframework.http.ResponseEntity;

public interface ITokenRefreshService {
    RefreshToken createRefreshToken(String username);
    ResponseEntity<JwtResponseDTO> refreshToken(RefreshTokenDTO token);
    RefreshToken findByToken(String token);

}
