package com.adamstraub.tonsoftacos.controllers.menuItemControllers;

import com.adamstraub.tonsoftacos.dto.categoryDto.ReturnedCategory;
import com.adamstraub.tonsoftacos.entities.MenuItem;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Validated
@RequestMapping("api/menu")
@OpenAPIDefinition(info = @Info(title = "End point documentation for the Tons of Tacos food truck application."),
        servers = {@Server(url="http://localhost:8080/", description = "Local server")})
public interface MenuItemControllerInterface {
    @Operation(
            summary = "Return menu item by id.",
            description = "For use by customers to view individual menu items and create an order."
            + "\n"  + "\n" + "Example response: "  + "\n"  + "\n" +
                    """
                            {
                                "id": 2,
                                "category": "taco",
                                "description": "nom nom",
                                "itemName": "golden pound",
                                "itemSize": null,
                                "unitPrice": 5.3
                            }
                            """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "A menu-item is returned."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Request parameters invalid."),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No menu-id found according to input."),
                    @ApiResponse(
                            responseCode = "500",
                            description = "An unplanned error occured."),

            },
            parameters = {
                    @Parameter(name = "id", allowEmptyValue = false, required = false),
            }
    )

    @GetMapping("/{id}")
    MenuItem getById(
            @RequestParam
            Integer id);



    @Operation(
            summary = "Return menu items by category.",
            description = """
                    For use by customers to view menu items by category and create an order." +
                    Current options include 'taco', 'side', 'drink', and 'topping'. Returns an array of menu items."""
                    + "\n"  + "\n" + "Example: "  + "\n"  + "\n" +
                    "api/menu/category?category=taco"
                    + "\n"  + "\n" + "Example response: "  + "\n"  + "\n" +
                    """
                            [
                                {
                                    "id": 1,
                                    "category": "taco",
                                    "description": "nom nom",
                                    "itemName": "pound",
                                    "itemSize": null,
                                    "unitPrice": 2.25
                                },
                                {
                                    "id": 2,
                                    "category": "taco",
                                    "description": "nom nom",
                                    "itemName": "golden pound",
                                    "itemSize": null,
                                    "unitPrice": 5.3
                                }
                            ]
                            """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Menu items returned by category."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Request parameters invalid."),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No menu-items found according to input. Check formatting"),
                    @ApiResponse(
                            responseCode = "500",
                            description = "An unplanned error occured."),

            },
            parameters = {
                    @Parameter(name = "category", allowEmptyValue = false, required = false),
            }
    )

    @GetMapping("/category")
    List<MenuItem> getByCategory(
            @RequestParam(required = true)
            String category);

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

