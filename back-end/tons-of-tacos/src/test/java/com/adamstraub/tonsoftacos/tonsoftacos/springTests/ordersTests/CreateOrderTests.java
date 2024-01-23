package com.adamstraub.tonsoftacos.tonsoftacos.springTests.ordersTests;
import com.adamstraub.tonsoftacos.dto.businessDto.OrderItemReturnedToOwner;
import com.adamstraub.tonsoftacos.dto.customerDto.ordersDto.OrderReturnedToCustomer;
import com.adamstraub.tonsoftacos.dto.businessDto.OrderReturnedToOwner;
import com.adamstraub.tonsoftacos.entities.OrderItem;
import com.adamstraub.tonsoftacos.tonsoftacos.testSupport.ordersTestsSupport.OrdersTestsSupport;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CreateOrderTests {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Nested
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    @TestPropertySource("classpath:test-application.properties")
    @Sql(scripts = {
            "classpath:/test-schema.sql",
            "classpath:/test-data.sql",
    },
            config = @SqlConfig(encoding = "utf-8"))
    class testThatDoesNotPolluteTheApplicationContextUris extends OrdersTestsSupport {
        @Test
        void orderCreated201() {
//           Given: a valid order and authheader

            // get valid token for authheader to search for the newly created order
            String token = encryptedToken();
            Assertions.assertNotNull(token);

//           build authheader
            HttpHeaders authHeader = new HttpHeaders();
            authHeader.setContentType(MediaType.APPLICATION_JSON);
            authHeader.setBearerAuth(token);
            HttpEntity<String> headerEntity = new HttpEntity<>(authHeader);

            String body = genUidBody();
            System.out.println("valid token generated in order to search for newly created order.");
            System.out.println("valid order body: " + body);


//                When: a successful connection is made
            String uri = getBaseUriForCreateOrder();
            System.out.println(uri);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> bodyEntity = new HttpEntity<>(body, headers);
            ResponseEntity<OrderReturnedToCustomer> response = restTemplate.getRestTemplate().exchange(uri, HttpMethod.POST, bodyEntity,
                    OrderReturnedToCustomer.class);

//            Then: an order is successfully stored with a 201 response
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            System.out.println("Response code is " + response.getStatusCode() + ".");
            System.out.println("response body: " + response.getBody());

//                And: The order is successfully retrieved by the test Uid as verification
            String parameter = "orderUid";
            String testOrderUid = Objects.requireNonNull(response.getBody()).getOrderUid();
            String getOrderUri =
                    String.format("%s?%s=%s", getBaseUriForGetOrderByUid(), parameter, testOrderUid);
            System.out.println(getOrderUri);
            ResponseEntity<OrderReturnedToOwner> orderUidResponse =
                    getRestTemplate().exchange(getOrderUri, HttpMethod.GET, headerEntity, new ParameterizedTypeReference<>() {
                    });

            Assertions.assertEquals(testOrderUid, Objects.requireNonNull(orderUidResponse.getBody()).getOrderUid());
            System.out.println(orderUidResponse.getBody());
            System.out.println("Response code is " + orderUidResponse.getStatusCode() + ".");
            System.out.println("Newly created order was found which verifies proper functionality.");

            BigDecimal orderItemsTotal = BigDecimal.valueOf(0.00);
            List<OrderItemReturnedToOwner> orderItems = new ArrayList<>(orderUidResponse.getBody().getOrderItems());

            for (OrderItemReturnedToOwner orderItem : orderItems){
            orderItemsTotal = orderItemsTotal.add(orderItem.getTotal());
            }
            Assertions.assertEquals(orderUidResponse.getBody().getOrderTotal(), orderItemsTotal);
            System.out.println("And order total calculated correctly: " + orderUidResponse.getBody().getOrderTotal().equals(orderItemsTotal));

        }


        @Test
        void invalidOrder400() {
//        Given: an invalid order(ie incomplete. missing fields, bad formatting etc) but valid auth header
//          get valid token for authheader
            String token = encryptedToken();
            Assertions.assertNotNull(token);

//           build authheader
            HttpHeaders authHeader = new HttpHeaders();
            authHeader.setContentType(MediaType.APPLICATION_JSON);
            authHeader.setBearerAuth(token);
            HttpEntity<String> headerEntity = new HttpEntity<>(authHeader);

//          invalid order
            String body = improperlyFormattedOrder();
            System.out.println("invalid order body: " + body);


//        When: a successful connection is made
            String uri = getBaseUriForCreateOrder();
            System.out.println(uri);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> bodyEntity = new HttpEntity<>(body, headers);
            ResponseEntity<Map<String, Object>> response = getRestTemplate().exchange(uri, HttpMethod.POST, bodyEntity, new ParameterizedTypeReference<>() {
            });
//        Then: a 400 bad request is returned as validation takes place in service
            Assertions.assertSame(response.getStatusCode(), HttpStatus.BAD_REQUEST);
            System.out.println("Response code is " + response.getStatusCode() + ".");
            System.out.println("response body: " + response.getBody());
//        And: the error message contains
            Map<String, Object> error = response.getBody();
            assert error != null;
            Assertions.assertEquals(error.get("status code").toString().substring(0,3), HttpStatus.BAD_REQUEST.toString().substring(0,3));
            Assertions.assertTrue(error.containsValue("/api/order/checkout"));
            Assertions.assertTrue(error.containsKey("message"));
            Assertions.assertTrue(error.containsKey("timestamp"));
            System.out.println("Test for unsuccessful use case complete.");
        }
    }
}
