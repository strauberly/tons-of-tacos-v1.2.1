package com.adamstraub.tonsoftacos.dto.businessDto.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenReq {
    private String refreshToken;
}
