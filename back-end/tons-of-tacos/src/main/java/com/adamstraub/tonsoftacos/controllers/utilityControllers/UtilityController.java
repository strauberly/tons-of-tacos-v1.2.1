package com.adamstraub.tonsoftacos.controllers.utilityControllers;

import com.adamstraub.tonsoftacos.dto.categoryDto.ReturnedCategory;
import com.adamstraub.tonsoftacos.services.utilityService.UtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UtilityController implements UtilityControllerInterface{

    @Autowired
    private UtilityService utilityService;

    @Override
    public List<ReturnedCategory> getAvailableCategories() {
        System.out.println("controller");
        return utilityService.getCategories();
    }
}
