package com.adamstraub.tonsoftacos.repository;

import com.adamstraub.tonsoftacos.entities.RefreshToken;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    RefreshToken findByToken(String token) throws EntityNotFoundException;

}
