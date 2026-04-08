package com.adamstraub.tonsoftacos.repository;

import com.adamstraub.tonsoftacos.entities.MenuItem;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Integer>{
        List<MenuItem> findByCategory(@RequestParam("category")String category) throws EntityNotFoundException;
}




