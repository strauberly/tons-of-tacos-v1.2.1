package com.adamstraub.tonsoftacos.tonsoftacos.testSupport.menuItemTestsSupport;

import com.adamstraub.tonsoftacos.tonsoftacos.testSupport.TestUris;

public class CreateMenuItemTestSupportUris extends TestUris {
        protected String createMenuItemBody(){
            return "{\n" +
                    " \"category\" : \"taco\", \n" +
                    " \"description\" : \"nom-nom\", \n" +
                    " \"item_name\" : \"ton\", \n" +
                    " \"item_size\" : \"NULL\", \n" +
                    " \"unit_price\" : \"2.25\", \n" +
                    "}";
        }
    }




