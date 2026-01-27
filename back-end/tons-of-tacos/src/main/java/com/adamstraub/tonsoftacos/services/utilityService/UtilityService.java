package com.adamstraub.tonsoftacos.services.utilityService;

import com.adamstraub.tonsoftacos.dao.CategoryRepository;
import com.adamstraub.tonsoftacos.dto.categoryDto.ReturnedCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class UtilityService implements UtitilyServiceInterface{

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    @Override
    public List<ReturnedCategory> getCategories() {
        System.out.println("service");
        return categoryRepository.getByAvailable();
    }
}
