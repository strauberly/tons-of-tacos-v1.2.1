package com.adamstraub.tonsoftacos.controllers.menuItemControllers;

import com.adamstraub.tonsoftacos.dto.categoryDto.ReturnedCategory;
import com.adamstraub.tonsoftacos.entities.MenuItem;
import com.adamstraub.tonsoftacos.services.menuItemService.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class MenuItemController implements MenuItemControllerInterface {
    @Autowired
    private MenuItemService menuItemService;


    @Override
    public ResponseEntity<List<MenuItem>> getByCategory(String category) {
        System.out.println("controller");
        return menuItemService.findByCategory(category);
    }

    @Override
    public ResponseEntity<List<ReturnedCategory>> getAvailableCategories() {
        return menuItemService.getCategories();
    }
}