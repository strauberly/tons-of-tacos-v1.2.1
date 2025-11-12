package com.adamstraub.tonsoftacos.dao;

import com.adamstraub.tonsoftacos.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
}
