package com.adamstraub.tonsoftacos.services.menuItemService;

import com.adamstraub.tonsoftacos.dto.categoryDto.DTO;
import com.adamstraub.tonsoftacos.entities.Category;
import com.adamstraub.tonsoftacos.entities.MenuItem;
import com.adamstraub.tonsoftacos.repository.CategoryRepository;
import com.adamstraub.tonsoftacos.repository.MenuItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class MenuItemService implements MenuItemServiceInterface{
    @Autowired
    private MenuItemRepository menuItemRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ResponseEntity<List<MenuItem>> findByCategory(String category){
    List<MenuItem> menuItems = menuItemRepository.findByCategory(category);
    if(menuItems.isEmpty()){
        throw new EntityNotFoundException("You have chosen a category: " + category + "," +
                " that does not exist. Please check your spelling and formatting.");
    }
    return ResponseEntity.ok(menuItems);
    }

    @Override
    public ResponseEntity<List<DTO>>getCategories(){
        List<DTO> categories;
        try{
            categories = getByAvailable();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(categories);
    }

    private List<DTO> getByAvailable(){
        Iterable<Category> iterable = categoryRepository.findByAvailableLike('y');
        return StreamSupport.stream(iterable.spliterator(),false).map(category -> {
            DTO dto = new DTO();
            BeanUtils.copyProperties(category,dto);
            return dto;
        }).collect(Collectors.toList());
    }


}
