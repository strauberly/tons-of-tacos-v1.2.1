package com.adamstraub.tonsoftacos.services.menuItemService;
import com.adamstraub.tonsoftacos.dao.MenuItemRepository;
import com.adamstraub.tonsoftacos.entities.MenuItem;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuItemService implements MenuItemServiceInterface {
    @Autowired
    private MenuItemRepository menuItemRepository;
    @Transactional
    @Override
    public MenuItem findById(Integer id) {
        System.out.println(" menu item service");
        MenuItem menuItem;

        try{
            menuItem = menuItemRepository.findById(id).orElseThrow();
        }catch (Exception e){
            throw new EntityNotFoundException("You have chosen a menu item that does not exist.");
        }
        return menuItem;
    }


    @Transactional(readOnly = true)
    @Override
    public List<MenuItem> findByCategory(String category) {
        System.out.println(" menu item serviceservice");
            List<MenuItem> menuItems = menuItemRepository.findByCategory(category);
            if (menuItems.isEmpty()){
                throw new EntityNotFoundException("You have chosen a category that does not exist. Please check your spelling and formatting.");
        }
            return menuItems;

    }
}
