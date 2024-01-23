package com.adamstraub.tonsoftacos.tonsoftacos.springTests.menuItemTests;
import com.adamstraub.tonsoftacos.entities.MenuItem;
import com.adamstraub.tonsoftacos.tonsoftacos.testSupport.menuItemTestsSupport.GetMenuItemsTestsSupport;
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
import static org.assertj.core.api.Assertions.assertThat;


import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BooleanSupplier;


class GetMenuItemsByCategoryTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Nested
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    @TestPropertySource("classpath:/test-application.properties")
    @Sql(scripts = {
            "classpath:/test-schema.sql",
            "classpath:/test-data.sql",
    },
            config = @SqlConfig(encoding = "utf-8"))
    class testThatDoesNotPolluteTheApplicationContextUris extends GetMenuItemsTestsSupport {

        @Test
        void menuItemsAreReturnedByCategoryWith200() {
            System.out.println(getBaseUriForMenuItemByCategoryQuery());
//        Given: a valid menu id category
            String categoryName = "tacos";
            String parameter = "category";
            String uri =
                    String.format("%s?%s=%s", getBaseUriForMenuItemByCategoryQuery(), parameter, categoryName);
            System.out.println(uri);


//         When: when connection is made to uri
            ResponseEntity<List<MenuItem>> response =
                    getRestTemplate().exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                    });
//            Then: a 200 ok code is returned
            System.out.println(("Response code is " + response.getStatusCode() + "."));
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//           And: returned items category = category entered for query
            LinkedList<MenuItem> returnedCategoryItems = new LinkedList<>(Objects.requireNonNull(response.getBody()));
            Assertions.assertEquals(returnedCategoryItems.get(0).getCategory(), categoryName);
            System.out.println("Returned category = " + returnedCategoryItems.get(0).getCategory() + ". Queried " +
                    "category = " + categoryName);
        }

        @Test
        void badCategoryEntryReturns404(){
//          Given: a category that does not exist
            String badInput = "!#%$^";
            String parameter = "category";
            String uri =
                    String.format("%s?%s=%s", getBaseUriForMenuItemByCategoryQuery(), parameter, badInput);
//          When: a connection is made
            ResponseEntity<Map<String, Object>> response =
                    getRestTemplate().exchange(uri, HttpMethod.GET, null,
                            new ParameterizedTypeReference<>() {
                            });
            System.out.println(uri);
//          Then: a 404 not found status code is returned
            Assertions.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
            System.out.println(("Response code is " + response.getStatusCode() + "."));
            System.out.println(response.getBody());
//          And: the error message contains
            Map<String, Object> error = response.getBody();
            assert error != null;
            Assertions.assertEquals(error.get("status code").toString().substring(0,3), HttpStatus.NOT_FOUND.toString().substring(0,3));
            Assertions.assertTrue(error.containsValue("/api/menu/category"));
            Assertions.assertTrue(error.containsKey("message"));
            Assertions.assertTrue(error.containsKey("timestamp"));
        }
    }
}
