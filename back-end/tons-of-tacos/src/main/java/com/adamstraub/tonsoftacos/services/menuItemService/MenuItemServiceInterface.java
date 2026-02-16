package com.adamstraub.tonsoftacos.services.menuItemService;

import com.adamstraub.tonsoftacos.dto.categoryDto.ReturnedCategory;
import com.adamstraub.tonsoftacos.entities.MenuItem;
import org.springframework.http.ResponseEntity;


import java.util.List;

public interface MenuItemServiceInterface {
ResponseEntity<List<MenuItem>> findByCategory(String category);
    List<ReturnedCategory>getCategories();
}
