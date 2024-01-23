package com.adamstraub.tonsoftacos.services.menuItemServices;

import com.adamstraub.tonsoftacos.entities.MenuItem;


import java.util.List;

public interface MenuItemServiceInterface {
    MenuItem findById(Integer id);

    List<MenuItem> findByCategory(String category);
}
