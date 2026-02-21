package com.adamstraub.tonsoftacos.services.menuItemService;
import com.adamstraub.tonsoftacos.dao.CategoryRepository;
import com.adamstraub.tonsoftacos.dao.MenuItemRepository;
import com.adamstraub.tonsoftacos.dto.categoryDto.ReturnedCategory;
import com.adamstraub.tonsoftacos.entities.MenuItem;
import com.adamstraub.tonsoftacos.exceptionHandler.GlobalExceptionHandler;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuItemService implements MenuItemServiceInterface {
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MenuItemRepository menuItemRepository;



    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<List<MenuItem>> findByCategory(String category) {
        System.out.println(" menu item service");
        System.out.println(category);

            List<MenuItem> menuItems = menuItemRepository.findByCategory(category);

            if (menuItems.isEmpty()) {
                throw new EntityNotFoundException("You have chosen a category: " + category + ", that does not exist. Please check your spelling and formatting.");
            }
            return ResponseEntity.ok(menuItems);
    }
//bring up to date with the above and implement try catch
    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<List<ReturnedCategory>> getCategories() {
        System.out.println("menu item service");
        List<ReturnedCategory> categories;
        try{
             categories = categoryRepository.getByAvailable();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(categories);
    }
}
