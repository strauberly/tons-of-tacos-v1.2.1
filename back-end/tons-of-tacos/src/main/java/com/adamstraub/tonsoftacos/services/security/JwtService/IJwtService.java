package com.adamstraub.tonsoftacos.services.security.JwtService;

import com.adamstraub.tonsoftacos.dto.securityDto.SubjectDTO;

public interface IJwtService {
    String generateToken(SubjectDTO subject);
}
