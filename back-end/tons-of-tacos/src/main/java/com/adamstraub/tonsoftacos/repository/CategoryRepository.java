package com.adamstraub.tonsoftacos.repository;


import com.adamstraub.tonsoftacos.entities.Category;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByAvailableLike(Character available) throws EntityNotFoundException;

}
