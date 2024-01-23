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

public class OrderReadyTests {

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
        void orderMarkedReadyWith200() {
//            Given: a valid order id and valid authheader
            //            get valid token
            String token = encryptedToken();
            Assertions.assertNotNull(token);

//           build authheader
            HttpHeaders authHeader = new HttpHeaders();
            authHeader.setContentType(MediaType.APPLICATION_JSON);
            authHeader.setBearerAuth(token);
            HttpEntity<String> headerEntity = new HttpEntity<>(authHeader);

            String orderUid = "654654-465465-555";

//            When: a connection is made
            String uri =
                    String.format("%s/%s", getBaseUriForOrderReady(), orderUid);
            System.out.println(uri);

            ResponseEntity<OrderReturnedToOwner> response =
                    getRestTemplate().exchange(uri, HttpMethod.PUT, headerEntity, new ParameterizedTypeReference<>() {
                    });
//            Then: order is marked ready and response code is 200
            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
            System.out.println("Response code is " + response.getStatusCode() + ".");

//            And: when the order is called ready does not == no
            String parameter = "orderUid";
            String getOrderUri =
                    String.format("%s?%s=%s", getBaseUriForGetOrderByUid(), parameter, orderUid);
            System.out.println(getOrderUri);
            ResponseEntity<OrderReturnedToOwner> getOrderResponse =
                    getRestTemplate().exchange(getOrderUri, HttpMethod.GET, headerEntity, new ParameterizedTypeReference<>() {
                    });
            Assertions.assertNotEquals("no", Objects.requireNonNull(getOrderResponse.getBody()).getReady());
            System.out.println("Order ready as of: " + Objects.requireNonNull(getOrderResponse.getBody()).getReady());

        }

        @Test
        void orderNotFound404() {
//            Given: an invalid order id and valid authheader
            //            get valid token
            String token = encryptedToken();
            Assertions.assertNotNull(token);

//           build authheader
            HttpHeaders authHeader = new HttpHeaders();
            authHeader.setContentType(MediaType.APPLICATION_JSON);
            authHeader.setBearerAuth(token);
            HttpEntity<String> headerEntity = new HttpEntity<>(authHeader);

            String orderId = "88";
//            When: a connection is made
            String uri =
                    String.format("%s/%s", getBaseUriForOrderReady(), orderId);
            System.out.println(uri);

            ResponseEntity<Map<String, Object>> response =
                    getRestTemplate().exchange(uri, HttpMethod.PUT, headerEntity, new ParameterizedTypeReference<>() {
                    });
//           Then: a 404 response is returned if no orders found
            org.assertj.core.api.Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            System.out.println(("Response code is " + response.getStatusCode() + "."));
            System.out.println("Response body: " + response.getBody());
            System.out.println("Negative test case complete for order status not changed if id invalid.");
        }
    }
}


