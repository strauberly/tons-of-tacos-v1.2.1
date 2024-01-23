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

public class OrderClosedTests {
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
        void orderMarkedClosedWith200() {
//            Given: a valid order id with valid auth header

            String orderUid = "654654-465465-555";

            //            get valid token
            String token = encryptedToken();
            Assertions.assertNotNull(token);

//           build authheader
            HttpHeaders authHeader = new HttpHeaders();
            authHeader.setContentType(MediaType.APPLICATION_JSON);
            authHeader.setBearerAuth(token);
            HttpEntity<String> headerEntity = new HttpEntity<>(authHeader);

//            connect and mark order ready

            String statusUri =
                    String.format("%s/%s", getBaseUriForOrderReady(), orderUid);
            System.out.println(statusUri);

            ResponseEntity<OrderReturnedToOwner> statusResponse =
                    getRestTemplate().exchange(statusUri, HttpMethod.PUT, headerEntity, new ParameterizedTypeReference<>() {
                    });

            System.out.println(statusResponse.getStatusCode());
            System.out.println(statusResponse.getBody());



//            get order and verify its status is ready
            //            get valid token
            String token2 = encryptedToken();
            Assertions.assertNotNull(token2);

//           build authheader
            HttpHeaders authHeader2 = new HttpHeaders();
            authHeader2.setContentType(MediaType.APPLICATION_JSON);
            authHeader2.setBearerAuth(token2);
            HttpEntity<String> headerEntity2 = new HttpEntity<>(authHeader2);

            String parameter1 = "orderUid";
            String verificationUri =
                    String.format("%s?%s=%s", getBaseUriForGetOrderByUid(), parameter1, orderUid);
            System.out.println(verificationUri);

            ResponseEntity<OrderReturnedToOwner> verificationResponse =
                    getRestTemplate().exchange(verificationUri, HttpMethod.GET, headerEntity2, new ParameterizedTypeReference<>() {
                    });

            Assertions.assertEquals(HttpStatus.OK, verificationResponse.getStatusCode());
            System.out.println(verificationResponse.getStatusCode());
            System.out.println(verificationResponse.getBody());
            System.out.println("Order ready: " + (!Objects.equals(Objects.requireNonNull(verificationResponse.getBody()).getReady(), "no")));


//            When: a connection is made to close order
            String uri =
                    String.format("%s/%s", getBaseUriForCloseOrder(), orderUid);
            System.out.println(uri);

            ResponseEntity<String> response =
                    getRestTemplate().exchange(uri, HttpMethod.PUT, headerEntity2, new ParameterizedTypeReference<>() {
                    });
//            Then: order is marked closed and response code is 200
            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
            System.out.println("Response code is " + response.getStatusCode() + ".");

            //            And: when the order is called the closed is indeed closed

            String parameter2 = "orderUid";
            String getOrderUri =
                    String.format("%s?%s=%s", getBaseUriForGetOrderByUid(), parameter2, orderUid);
            System.out.println(getOrderUri);
            ResponseEntity<OrderReturnedToOwner> getOrderResponse =
                    getRestTemplate().exchange(getOrderUri, HttpMethod.GET, headerEntity2, new ParameterizedTypeReference<>() {
                    });
            System.out.println(getOrderResponse.getBody());
            Assertions.assertNotEquals("no", Objects.requireNonNull(getOrderResponse.getBody()).getReady());
            System.out.println("Response code is " + getOrderResponse.getStatusCode() + ".");
            System.out.println("Order closed as of : " + Objects.requireNonNull(getOrderResponse.getBody()).getClosed());
            System.out.println("Successful test case for closing an order complete.");
        }

        @Test
        void orderDoesNotExist404() {
//            Given: an invalid order id with valid auth header
            String orderUid = "66";
            //            get valid token
            String token = encryptedToken();
            Assertions.assertNotNull(token);

//           build authheader
            HttpHeaders authHeader = new HttpHeaders();
            authHeader.setContentType(MediaType.APPLICATION_JSON);
            authHeader.setBearerAuth(token);
            HttpEntity<String> headerEntity = new HttpEntity<>(authHeader);


//            When: a connection is made to close order
            String uri =
                    String.format("%s/%s", getBaseUriForCloseOrder(), orderUid);
            System.out.println(uri);

            ResponseEntity<Map<String, Object>> response =
                    getRestTemplate().exchange(uri, HttpMethod.PUT, headerEntity, new ParameterizedTypeReference<>() {
                    });
//            Then: a 404 not found is returned
            Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            System.out.println("Response code is " + response.getStatusCode() + ".");
            System.out.println(response.getBody());
            System.out.println("Negative test case complete for try to close order with invalid order id.");
        }

        @Test
        void orderNotReadyBeforeClosing400() {
//            Given: a valid order id with valid auth header but has not been marked ready
            String orderUid = "654654-465465-555";

            //            get valid token
            String token = encryptedToken();
            Assertions.assertNotNull(token);

//           build authheader
            HttpHeaders authHeader = new HttpHeaders();
            authHeader.setContentType(MediaType.APPLICATION_JSON);
            authHeader.setBearerAuth(token);
            HttpEntity<String> headerEntity = new HttpEntity<>(authHeader);

//            When: a connection is made to close order
            String uri =
                    String.format("%s/%s", getBaseUriForCloseOrder(), orderUid);
            System.out.println(uri);

            ResponseEntity<Map<String, Object>> response =
                    getRestTemplate().exchange(uri, HttpMethod.PUT, headerEntity, new ParameterizedTypeReference<>() {
                    });
//            Then: a 400 response is returned
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            System.out.println("Response code is " + response.getStatusCode() + ".");
            System.out.println(response.getBody());
            System.out.println("Negative test case complete for order not closed if order is not ready.");
//

        }
    }
}

