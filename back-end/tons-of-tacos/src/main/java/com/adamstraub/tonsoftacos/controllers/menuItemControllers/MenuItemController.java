package com.adamstraub.tonsoftacos.controllers.menuItemControllers;

import com.adamstraub.tonsoftacos.entities.MenuItem;
import com.adamstraub.tonsoftacos.services.menuItemServices.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class MenuItemController implements MenuItemControllerInterface {
    @Autowired
    private MenuItemService menuItemService;

    @Override
    public MenuItem getById(Integer id) {
        System.out.println("controller");
        return menuItemService.findById(id);
    }
    @Override
    public List<MenuItem> getByCategory(String category) {
        System.out.println("controller");
        return menuItemService.findByCategory(category);
    }
}