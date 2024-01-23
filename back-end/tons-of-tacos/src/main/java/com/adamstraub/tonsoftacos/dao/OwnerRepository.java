package com.adamstraub.tonsoftacos.dao;

import com.adamstraub.tonsoftacos.entities.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Integer> {
    Optional<Owner> findByUsername(String username);
}
