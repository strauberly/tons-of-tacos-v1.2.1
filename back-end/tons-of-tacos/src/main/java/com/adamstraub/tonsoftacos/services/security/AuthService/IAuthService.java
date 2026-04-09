package com.adamstraub.tonsoftacos.services.security.AuthService;

import com.adamstraub.tonsoftacos.dto.businessDto.ResponseMessageDTO;
import com.adamstraub.tonsoftacos.dto.securityDto.JwtResponseDTO;
import com.adamstraub.tonsoftacos.dto.securityDto.OwnerAuthDTO;
import com.adamstraub.tonsoftacos.dto.securityDto.RefreshTokenDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;

public interface IAuthService {
    ResponseEntity<JwtResponseDTO> ownerLogin(@NotNull HttpServletRequest request, OwnerAuthDTO ownerAuth);
    ResponseEntity<ResponseMessageDTO> ownerLogout(@NotNull HttpServletRequest request, RefreshTokenDTO token);
}
