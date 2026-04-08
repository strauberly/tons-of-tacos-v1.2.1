package com.adamstraub.tonsoftacos.repository;

import com.adamstraub.tonsoftacos.entities.Owner;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Integer> {
    Optional<Owner> findByUsername(String username)throws EntityNotFoundException;
}
