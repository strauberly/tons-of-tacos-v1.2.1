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

public class GetOrderByUidTests {

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
        void validOrderIsReturnedByUidWith200() {
//            Given: a valid uid and authheader

            //            get valid token
            String token = encryptedToken();
            Assertions.assertNotNull(token);

//           build authheader
            HttpHeaders authHeader = new HttpHeaders();
            authHeader.setContentType(MediaType.APPLICATION_JSON);
            authHeader.setBearerAuth(token);
            HttpEntity<String> headerEntity = new HttpEntity<>(authHeader);

            String parameter = "orderUid";
            String testOrderUid = "654654-4655-555";
//            When: a connection is made
            String uri =
                    String.format("%s?%s=%s", getBaseUriForGetOrderByUid(), parameter, testOrderUid );
            System.out.println(uri);
            System.out.println("Order returned from db: ");
            ResponseEntity<OrderReturnedToOwner> response =
                    getRestTemplate().exchange(uri, HttpMethod.GET, headerEntity, new ParameterizedTypeReference<>() {
                    });
//            Then: an order is returned with a uid matching the test uid and a 200 response code
            Assertions.assertEquals(testOrderUid, Objects.requireNonNull(response.getBody()).getOrderUid());
            System.out.println("Response code is " + response.getStatusCode() + ".");
            System.out.println(response.getBody());
            System.out.println("Queried uuid: " +testOrderUid+ " =  Uid of order returned: " + response.getBody().getOrderUid());
        }

        @Test
        void invalidUidReturns404() {
//            Given: an invalid uid and valid authheader

            //            get valid token
            String token = encryptedToken();
            Assertions.assertNotNull(token);

//           build authheader
            HttpHeaders authHeader = new HttpHeaders();
            authHeader.setContentType(MediaType.APPLICATION_JSON);
            authHeader.setBearerAuth(token);
            HttpEntity<String> headerEntity = new HttpEntity<>(authHeader);

            String parameter = "orderUid";
            String testOrderUid = "asd45s";
//            When: a connection is made
            String uri =
                    String.format("%s?%s=%s", getBaseUriForGetOrderByUid(), parameter, testOrderUid );
            System.out.println(uri);

            ResponseEntity<Map<String, Object>> response =
                    getRestTemplate().exchange(uri, HttpMethod.GET, headerEntity, new ParameterizedTypeReference<>() {
                    });
//            Then: a 404 response is returned
            Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            System.out.println("Response code is " + response.getStatusCode() + ".");
            System.out.println(response.getBody());
            System.out.println("Negative test case complete for no order returned with invalid uid.");
        }
    }
}

