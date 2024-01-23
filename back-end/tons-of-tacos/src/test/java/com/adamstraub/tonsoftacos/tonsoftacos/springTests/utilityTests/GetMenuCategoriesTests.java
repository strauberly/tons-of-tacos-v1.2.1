package com.adamstraub.tonsoftacos.tonsoftacos.springTests.utilityTests;

import com.adamstraub.tonsoftacos.dao.CategoryRepository;
import com.adamstraub.tonsoftacos.dto.categoryDto.ReturnedCategory;
import com.adamstraub.tonsoftacos.entities.Category;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import com.adamstraub.tonsoftacos.tonsoftacos.testSupport.categoriesTestsSupport.GetMenuCategoriesTestsSupport;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class GetMenuCategoriesTests {
    @Autowired
   private JdbcTemplate jdbcTemplate;



    @Nested
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    @TestPropertySource("classpath:/test-application.properties")
    @Sql(scripts = {
            "classpath:/test-schema.sql",
            "classpath:/test-data.sql"
    },
            config = @SqlConfig(encoding = "utf-8"))

    class testThatDoesNotPolluteTheApplicationContextUris extends GetMenuCategoriesTestsSupport {
        @Autowired
        private CategoryRepository categoryRepository;
        @Test
        void categoriesCalledFromDBAreAvailable() {
            System.out.println("");

            boolean categoryLeaked = false;



            List<Category> menuCategories = categoryRepository.findByAvailableLike('y');
            System.out.println(menuCategories);
            String positive = "All is as it should no unavailable categories leaked.";

            String negative = "Something is amiss. Unavailable category returned.";

            for(Category category: menuCategories ){
                if (category.getAvailable() == 'n'){
                    categoryLeaked = true;
                }
            }
            System.out.println("Categories leaked: " + categoryLeaked);
            Assertions.assertFalse(categoryLeaked);
        }

        @Test
        void menuCategoriesReturnedWith200() throws JSONException {

            System.out.println(getBaseUriForReturnAvailableCategories());



            //            Given: the correct uri
            String uri = getBaseUriForReturnAvailableCategories();
//             When: a successful coneection is made
            ResponseEntity<List<ReturnedCategory>> response =
                    getRestTemplate().exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                    });

            System.out.println(Objects.requireNonNull(response.getBody()).get(0).getClass().getSimpleName());
//            System.out.println(Class.getSimpleName());

//            Then: a 200 code is returned
            Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
            System.out.println("Status code is: " + response.getStatusCode());
            Assertions.assertEquals(response.getBody().get(0).getClass().getSimpleName(), "ReturnedCategory");
            System.out.println("Class type in response body: " + response.getBody().get(0).getClass().getSimpleName());

//            And: body is not empty
            Assertions.assertFalse(Objects.requireNonNull(response.getBody()).isEmpty());
//            Assertions.assertTrue(response.getBody().containsAll());
            System.out.println(Objects.requireNonNull(response.getBody()));
            System.out.println("Menu categories successfully returned with a 200 status.");

        }
    }
}
