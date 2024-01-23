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
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class EditOrderItemQuantityTest {

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
        void editOrderItemQuantity200() {
//            Given: a valid order, order item, new quantity and auth header.

//            get valid token
            String token = encryptedToken();
            Assertions.assertNotNull(token);

//           build authheader
            HttpHeaders authHeader = new HttpHeaders();
            authHeader.setContentType(MediaType.APPLICATION_JSON);
            authHeader.setBearerAuth(token);
            HttpEntity<String> headerEntity = new HttpEntity<>(authHeader);

            String orderUid = "654654-465465-555";
            int orderItemId = 3;
            int newQuantity = 2;

//             get order before alteration
            String parameter = "orderUid";
            String getOrderUri =
                    String.format("%s?%s=%s", getBaseUriForGetOrderByUid(), parameter, orderUid);

            System.out.println(getOrderUri);

//            call order item before alteration
            ResponseEntity<OrderReturnedToOwner> getOrderResponse =
                    getRestTemplate().exchange(getOrderUri, HttpMethod.GET, headerEntity, new ParameterizedTypeReference<>() {
                    });
            System.out.println("unchanged order: " + getOrderResponse.getBody());
//            When: a successful connection is made
            String uri =
                    String.format("%s/%s/%d/%d", getBaseUriForEditOrderItem(), orderUid, orderItemId, newQuantity);
            System.out.println(uri);
//
            ResponseEntity<String> response =
                    getRestTemplate().exchange(uri, HttpMethod.PUT, headerEntity,
                            new ParameterizedTypeReference<>() {});

//            Then: a response of 200 returned
            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
            System.out.println("Response code is " + response.getStatusCode() + ".");
            System.out.println("Response body: " + response.getBody());

//            And: when the order called the order item quantity, total, and order total should not be as before.

//            call order item after alteration
            String getOrderUri2 =
                    String.format("%s?%s=%s", getBaseUriForGetOrderByUid(), parameter, orderUid);
            System.out.println(getOrderUri2);

            ResponseEntity<OrderReturnedToOwner> getOrderResponse2 =
                    getRestTemplate().exchange(getOrderUri, HttpMethod.GET, headerEntity, new ParameterizedTypeReference<>() {
                    });

            System.out.println("altered order: " + getOrderResponse2.getBody());
            Assertions.assertNotEquals(getOrderResponse.getBody().getOrderTotal(), getOrderResponse2.getBody().getOrderTotal());
            System.out.println("Order total changed.");
            Assertions.assertNotEquals(getOrderResponse.getBody().getOrderItems().get(0).getQuantity(), getOrderResponse2.getBody().getOrderItems().get(0).getQuantity());
            System.out.println("Order item quantity changed.");
            Assertions.assertNotEquals(getOrderResponse.getBody().getOrderItems().get(0).getTotal(), getOrderResponse2.getBody().getOrderItems().get(0).getTotal());
            System.out.println("Order item total changed.");

        }

        @Test
        void orderToBeEditedNotFound404() {
//            Given: an invalid order, valid order item, valid quantity and valid auth header.

//            get valid token
            String token = encryptedToken();
            Assertions.assertNotNull(token);

//           build authheader
            HttpHeaders authHeader = new HttpHeaders();
            authHeader.setContentType(MediaType.APPLICATION_JSON);
            authHeader.setBearerAuth(token);
            HttpEntity<String> headerEntity = new HttpEntity<>(authHeader);

            String orderId = "77";
            int orderItemId = 3;
            int newQuantity = 2;

//            When: a successful connection is made
            String uri =
                    String.format("%s/%s/%d/%d", getBaseUriForEditOrderItem(), orderId, orderItemId, newQuantity);
            System.out.println(uri);
//
            ResponseEntity<Map<String, Object>> response =
                    getRestTemplate().exchange(uri, HttpMethod.PUT, headerEntity,
                            new ParameterizedTypeReference<>() {});
            System.out.println("Response code is " + response.getStatusCode() + ".");
            System.out.println("Response body: " + response.getBody());
//            Then: a response of 404 returned
            Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            System.out.println("Response code is " + response.getStatusCode() + ".");
            System.out.println("Response body: " + response.getBody());

//        And: the error message contains
            Map<String, Object> error = response.getBody();
            assert error != null;
            Assertions.assertEquals(error.get("status code").toString().substring(0,3), HttpStatus.NOT_FOUND.toString().substring(0,3));
            Assertions.assertTrue(error.containsValue("/api/owners-tools/orders/update-order-item/77/3/2"));
            Assertions.assertTrue(error.containsKey("message"));
            Assertions.assertTrue(error.containsKey("timestamp"));
            System.out.println("Negative test case complete for attempt to add item to invalid order.");

        }

        @Test
        void orderItemToBeEditedNotFound404() {
//            Given: a valid order, order item, new quantity and auth header.

//            get valid token
            String token = encryptedToken();
            Assertions.assertNotNull(token);

//           build authheader
            HttpHeaders authHeader = new HttpHeaders();
            authHeader.setContentType(MediaType.APPLICATION_JSON);
            authHeader.setBearerAuth(token);
            HttpEntity<String> headerEntity = new HttpEntity<>(authHeader);


            String orderUid = "654654-465465-555";
            int orderItemId = 88;
            int newQuantity = 2;

//            When: a successful connection is made
            String uri =
                    String.format("%s/%s/%d/%d", getBaseUriForEditOrderItem(), orderUid, orderItemId, newQuantity);
            System.out.println(uri);
//
            ResponseEntity<Map<String, Object>> response =
                    getRestTemplate().exchange(uri, HttpMethod.PUT, headerEntity,
                            new ParameterizedTypeReference<>() {});

//            Then: a response of 404 returned
            Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            System.out.println("Response code is " + response.getStatusCode() + ".");
            System.out.println("Response body: " + response.getBody());

//        And: the error message contains
            Map<String, Object> error = response.getBody();
            assert error != null;
            Assertions.assertEquals(error.get("status code").toString().substring(0,3), HttpStatus.NOT_FOUND.toString().substring(0,3));
            Assertions.assertTrue(error.containsValue("/api/owners-tools/orders/update-order-item/654654-465465-555/88/2"));
            Assertions.assertTrue(error.containsKey("message"));
            Assertions.assertTrue(error.containsKey("timestamp"));
            System.out.println("Negative test case complete for attempt to edit invalid order item.");
        }

        @Test
        void newQuantityOutOfBounds400() {
//            Given: a valid order, order item, new quantity and auth header.

//            get valid token
            String token = encryptedToken();
            Assertions.assertNotNull(token);

//           build authheader
            HttpHeaders authHeader = new HttpHeaders();
            authHeader.setContentType(MediaType.APPLICATION_JSON);
            authHeader.setBearerAuth(token);
            HttpEntity<String> headerEntity = new HttpEntity<>(authHeader);

            String orderUid = "654654-465465-555";
            int orderItemId = 3;
            int newQuantity = 12;
            System.out.println(newQuantity);

//            When: a successful connection is made
            String uri =
                    String.format("%s/%s/%d/%d", getBaseUriForEditOrderItem(), orderUid, orderItemId, newQuantity);

            System.out.println(uri);
//
            ResponseEntity<Map<String, Object>> response =
                    getRestTemplate().exchange(uri, HttpMethod.PUT, headerEntity,
                            new ParameterizedTypeReference<>() {});

//            Then: a response of 400 returned
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            System.out.println("Response code is " + response.getStatusCode() + ".");
            System.out.println("Response body: " + response.getBody());

//        And: the error message contains
            Map<String, Object> error = response.getBody();
            assert error != null;
            Assertions.assertEquals(error.get("status code").toString().substring(0,3), HttpStatus.BAD_REQUEST.toString().substring(0,3));
            Assertions.assertTrue(error.containsValue("/api/owners-tools/orders/update-order-item/654654-465465-555/3/12"));
            Assertions.assertTrue(error.containsKey("message"));
            Assertions.assertTrue(error.containsKey("timestamp"));
            System.out.println("Negative test case complete for attempt to add more than 10 of any item to an order.");
        }
    }
}

