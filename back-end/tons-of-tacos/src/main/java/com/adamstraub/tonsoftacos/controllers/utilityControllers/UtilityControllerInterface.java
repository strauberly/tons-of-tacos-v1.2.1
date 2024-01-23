package com.adamstraub.tonsoftacos.controllers.utilityControllers;
import com.adamstraub.tonsoftacos.dto.categoryDto.ReturnedCategory;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Validated
@RequestMapping(
        value = "api/utility")
@OpenAPIDefinition (info = @Info(title = "Utility functions."),
        servers = {@Server(url = "http://localhost:8080", description = "Local server")})

public interface UtilityControllerInterface {
    @Operation(
            summary = "All categories marked with 'y' for availability are returned.",
            description = """
                    
                    This is a ulity function that allows developers to dynamically serve all menu 
                    categories and their descriptions if inteded to be available for viewing. The intent
                    is to provdie a means for the fictional owners of Tons of Tacos to create a new menu category 
                    but only provide it or its items when ready.
                    
                    """,

            responses = {
                    @ApiResponse(
                    responseCode = "200",
                    description = """
                            Menu catefogories and their descriptions returned if marked available
                            with a 'y'"""),
                    @ApiResponse(
                            responseCode = "500",
                            description = "An unplanned error occured."),
            }
    )

//    @Transactional
    @GetMapping ("/categories")
    List<ReturnedCategory> getAvailableCategories();
}
