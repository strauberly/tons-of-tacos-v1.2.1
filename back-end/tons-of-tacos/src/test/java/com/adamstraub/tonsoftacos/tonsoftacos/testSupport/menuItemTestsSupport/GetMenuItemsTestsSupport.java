package com.adamstraub.tonsoftacos.tonsoftacos.testSupport.menuItemTestsSupport;
import com.adamstraub.tonsoftacos.entities.MenuItem;
import com.adamstraub.tonsoftacos.tonsoftacos.testSupport.TestUris;

import java.math.BigDecimal;


public class GetMenuItemsTestsSupport extends TestUris {

    protected MenuItem sample() {

        return MenuItem.builder()
                 .id(1)
               .category("taco")
               .description("nom nom")
               .itemName("pound")
               .itemSize("NULL")
               .unitPrice(BigDecimal.valueOf(2.25))
               .build();
        }
    }


