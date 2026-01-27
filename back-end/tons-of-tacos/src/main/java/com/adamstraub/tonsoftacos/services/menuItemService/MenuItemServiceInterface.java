package com.adamstraub.tonsoftacos.services.menuItemService;

import com.adamstraub.tonsoftacos.entities.MenuItem;


import java.util.List;

public interface MenuItemServiceInterface {
    MenuItem findById(Integer id);

    List<MenuItem> findByCategory(String category);
}
