package com.adamstraub.tonsoftacos.dao;

import com.adamstraub.tonsoftacos.entities.RefreshToken;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    RefreshToken findByToken(String token);

    @NotNull
    List<RefreshToken> findAll();
}
