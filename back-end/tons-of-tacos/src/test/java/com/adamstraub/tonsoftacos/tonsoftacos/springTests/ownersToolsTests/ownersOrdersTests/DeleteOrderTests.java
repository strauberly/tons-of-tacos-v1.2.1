package com.adamstraub.tonsoftacos.tonsoftacos.springTests.ownersToolsTests.ownersOrdersTests;

import com.adamstraub.tonsoftacos.dto.businessDto.OrderReturnedToOwner;
import com.adamstraub.tonsoftacos.tonsoftacos.testSupport.ownersToolsSupport.OwnersToolsTestsSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DeleteOrderTests {

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
    class testThatDoesNotPolluteTheApplicationContextUris extends OwnersToolsTestsSupport {
        @Test
        void orderDeleted200() {
//        Given: given a valid order id and auth header.
            //            get valid token
            String token = encryptedToken();
            Assertions.assertNotNull(token);

//           build authheader
            HttpHeaders authHeader = new HttpHeaders();
            authHeader.setContentType(MediaType.APPLICATION_JSON);
            authHeader.setBearerAuth(token);
            HttpEntity<String> headerEntity = new HttpEntity<>(authHeader);

            String orderUid = "654654-465465-555";

            //        When: a connection is made
            String uri =
                    String.format("%s/%s", getBaseUriForDeleteOrder(), orderUid);
            System.out.println(uri);

            ResponseEntity<String> response =
                    getRestTemplate().exchange(uri, HttpMethod.DELETE, headerEntity, new ParameterizedTypeReference<>() {
                    });
//
//        Then: the order is removed from the database with a 200 response
            System.out.println("Response code is " + response.getStatusCode() + ".");
            System.out.println("Response body:  " + response.getBody());
            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
//        And: an attempt to call the order deleted will give a 404 response
//            String parameter = "orderId";
            String parameter = "orderUid";
            String getOrderUri =
                    String.format("%s?%s=%s", getBaseUriForGetOrderByUid(), parameter, orderUid);
            System.out.println(getOrderUri);
            ResponseEntity<OrderReturnedToOwner> getOrderResponse =
                    getRestTemplate().exchange(getOrderUri, HttpMethod.GET, headerEntity, new ParameterizedTypeReference<>() {
                    });
            System.out.println("Response code is " + getOrderResponse.getStatusCode() + ".");
            System.out.println("Body is " + getOrderResponse.getBody() + ".");
            System.out.println("Order has been deleted and can not be found.");
            Assertions.assertEquals(HttpStatus.NOT_FOUND, getOrderResponse.getStatusCode());
        }

        @Test
        void orderToBeDeletedInvalid404() {
//        Given: given an invalid order id and valid auth header.
            //            get valid token

            String token = encryptedToken();
            Assertions.assertNotNull(token);

//           build authheader
            HttpHeaders authHeader = new HttpHeaders();
            authHeader.setContentType(MediaType.APPLICATION_JSON);
            authHeader.setBearerAuth(token);
            HttpEntity<String> headerEntity = new HttpEntity<>(authHeader);

            int orderId = 78;

            //        When: a successful connection is made
            String uri =
                    String.format("%s/%s", getBaseUriForDeleteOrder(), orderId);
            System.out.println(uri);

            ResponseEntity<Map<String, Object>> response =
                    getRestTemplate().exchange(uri, HttpMethod.DELETE, headerEntity, new ParameterizedTypeReference<>() {
                    });

//
//        Then:  response will return 404
            Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            System.out.println("Response code is " + response.getStatusCode() + ".");
            System.out.println("Response body: " + response.getBody());

//        And: the error message contains
            Map<String, Object> error = response.getBody();
            assert error != null;
            Assertions.assertEquals(error.get("status code").toString().substring(0,3), HttpStatus.NOT_FOUND.toString().substring(0,3));
            Assertions.assertTrue(error.containsValue("/api/owners-tools/orders/delete-order/78"));
            Assertions.assertTrue(error.containsKey("message"));
            Assertions.assertTrue(error.containsKey("timestamp"));
            System.out.println("Negative test case complete for attempt to add item to invalid order.");

        }
    }
}

