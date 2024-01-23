package com.adamstraub.tonsoftacos.tonsoftacos.springTests.menuItemTests;
import com.adamstraub.tonsoftacos.dao.MenuItemRepository;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class GetMenuItemsByIdTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    MenuItemRepository menuItemRepository;

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
        void validMenuItemIsReturnedByIdWith200() {
            System.out.println(getBaseUriForMenuItemByIdQuery());
//        Given: a valid menu id
            int itemId = 1;


            //      When: Connection is made
            String parameter = "id";
            String uri =
                    String.format("%s?%s=%d", getBaseUriForMenuItemByIdQuery(), parameter, itemId);
            System.out.println(uri);

            ResponseEntity<MenuItem> response =
                    getRestTemplate().exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                    });
//            Then: A 200 closed code is returned
            System.out.println("Response code is " + response.getStatusCode() + ".");
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            //            And: it matches the expected outcome
            String actual = String.valueOf(Objects.requireNonNull(response.getBody()).getId());
            String expected = String.valueOf(sample().getId());
            System.out.println("Actual id returned is " + actual + ", and expected id is " + expected + ".");
            assertThat(actual).isEqualTo(expected);
            System.out.println(response.getBody());
        }

        @Test
        void nonExistentMenuItemReturns404() {
            System.out.println(getBaseUriForMenuItemByIdQuery());
//      Given: an invalid menu id
            int itemId = 45;
            String parameter = "id";
            String uri =
                    String.format("%s?%s=%d", getBaseUriForMenuItemByIdQuery(), parameter, itemId);
            System.out.println(uri);
//      When: Connection is made
            ResponseEntity<Map<String, Object>> response =
                    getRestTemplate().exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                    });
            System.out.println(response.getBody());
//      Then: A 404 is returned
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            System.out.println("Response code is " + response.getStatusCode() + ".");

            //      And: the error message contains
            Map<String, Object> error = response.getBody();
            assert error != null;
            Assertions.assertEquals(error.get("status code").toString().substring(0,3), HttpStatus.NOT_FOUND.toString().substring(0,3));
            Assertions.assertTrue(error.containsValue("/api/menu/id"));
            Assertions.assertTrue(error.containsKey("message"));
            Assertions.assertTrue(error.containsKey("timestamp"));
        }
    }
}


