package com.adamstraub.tonsoftacos.tonsoftacos.springTests.ownersToolsTests.authTests;

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

//provide an authorized endpoint and try to access with malformed jwts (ie any part left null, beyond expiration,
// bad signature, iat after expired)


public class BadJwtTests {
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
    class testThatDoesNotPolluteTheApplicationContextUris extends OwnersToolsTestsSupport {



        @Test
        void expiredToken403() {
//            Given: an expired token but valid auth header
            String token = expiredToken();
            Assertions.assertNotNull(token);
            System.out.println(token);

//           build authheader
            HttpHeaders authHeader = new HttpHeaders();
            authHeader.setContentType(MediaType.APPLICATION_JSON);
            authHeader.setBearerAuth(token);
            HttpEntity<String> headerEntity = new HttpEntity<>(authHeader);
            System.out.println(headerEntity);
            int customerId = 1;


            //        When: a successful connection is made to an endpoint requiring auth
            String uri=
                    String.format("%s/%d", getBaseUriForDeleteCustomer(), customerId);
            System.out.println(uri);


            ResponseEntity<Map<String, Object>> response =
                    getRestTemplate().exchange(uri, HttpMethod.DELETE, headerEntity, new ParameterizedTypeReference<>() {
                    });

            //          Then: a 403 FORBIDDEN response is returned
            Assertions.assertSame(response.getStatusCode(), HttpStatus.FORBIDDEN);
            System.out.println("Response code is " + response.getStatusCode() + ".");
            System.out.println("response body: " + response.getBody());
            //        And: the error message contains
            Map<String, Object> error = response.getBody();
            assert error != null;
            Assertions.assertEquals(error.get("status code").toString().substring(0,3), HttpStatus.FORBIDDEN.toString().substring(0,3));
            Assertions.assertTrue(error.containsValue("/api/owners-tools/customers/delete-customer/1"));
            Assertions.assertTrue(error.containsKey("message"));
            Assertions.assertTrue(error.containsKey("timestamp"));
            System.out.println("Test complete and expired jwt exception is caught and handled.");
        }

        @Test
        void invalidSubject401(){
//        Given: a jwt with an invalid subject
            String badToken = badToken();
            System.out.println(badToken);

            //           build authheader
            HttpHeaders authHeader = new HttpHeaders();
            authHeader.setContentType(MediaType.APPLICATION_JSON);
            authHeader.setBearerAuth(badToken);
            HttpEntity<String> headerEntity = new HttpEntity<>(authHeader);
            System.out.println(headerEntity);
            int customerId = 1;

//        When: a successful connection is made to an endpoint requiring auth
            String uri=
                    String.format("%s/%d", getBaseUriForDeleteCustomer(), customerId);
            System.out.println(uri);


            ResponseEntity<Map<String, Object>> response =
                    getRestTemplate().exchange(uri, HttpMethod.DELETE, headerEntity, new ParameterizedTypeReference<>() {
                    });
            System.out.println(response.getBody());
            System.out.println(response);
//        Then: a 403 FORBIDDEN is returned
            Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            System.out.println("Response code is " + response.getStatusCode() + ".");
            System.out.println("response body: " + response.getBody());

//        And: the exception message contains
            Map<String, Object> error = response.getBody();
            assert error != null;
            Assertions.assertTrue(error.containsValue("/api/owners-tools/customers/delete-customer/1"));
            Assertions.assertTrue(error.containsKey("message"));
            Assertions.assertTrue(error.containsKey("timestamp"));
            System.out.println("Test complete and invalid token is caught and handled.");
        }
    }
}

