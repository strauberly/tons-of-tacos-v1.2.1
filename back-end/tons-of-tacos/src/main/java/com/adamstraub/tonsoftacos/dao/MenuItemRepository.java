package com.adamstraub.tonsoftacos.dao;

import com.adamstraub.tonsoftacos.entities.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Integer>{
         MenuItem getById(@RequestParam("item_pk")  Integer id);

        List<MenuItem> findByCategory(@RequestParam("category")String category);
}




