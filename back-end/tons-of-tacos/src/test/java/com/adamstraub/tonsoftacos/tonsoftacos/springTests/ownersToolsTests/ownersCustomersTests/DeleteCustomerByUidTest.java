package com.adamstraub.tonsoftacos.tonsoftacos.springTests.ownersToolsTests.ownersCustomersTests;

import com.adamstraub.tonsoftacos.dto.businessDto.CustomerReturnedToOwner;
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

public class DeleteCustomerByUidTest {
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
        void customerDeleted200() {
            //        Given: given a valid customer id and auth header
//            get valid token
            String token = encryptedToken();
//            Assertions.assertNotNull(token);

//           build authheader
            HttpHeaders authHeader = new HttpHeaders();
            authHeader.setContentType(MediaType.APPLICATION_JSON);
            authHeader.setBearerAuth(token);
            HttpEntity<String> headerEntity = new HttpEntity<>(authHeader);
            System.out.println(headerEntity);

            String customerId = "gd34-igjr";

            //        When: a connection is made
            String uri=
                    String.format("%s/%s", getBaseUriForDeleteCustomer(), customerId);
            System.out.println(uri);


            ResponseEntity<String> response =
                    getRestTemplate().exchange(uri, HttpMethod.DELETE, headerEntity, new ParameterizedTypeReference<>() {
                    });
//
//        Then: the customer is removed from the database with a 200 response
            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
            System.out.println("Response code is " + response.getStatusCode() + ".");
            System.out.println("Response body: " + response.getBody() + ".");

//        And: an attempt to call the deleted customer will return a 404
            String parameter = "customerUid";
            String getCustomerUri =
                    String.format("%s?%s=%s", getBaseUriForGetCustomerByUid(), parameter, customerId);

            ResponseEntity<Map<String, Object>> getCustomerResponse =
                    getRestTemplate().exchange
                            (getCustomerUri, HttpMethod.GET, headerEntity, new ParameterizedTypeReference<>() {});
            Assertions.assertEquals(HttpStatus.NOT_FOUND, getCustomerResponse.getStatusCode());

            System.out.println("Response code is " + getCustomerResponse.getStatusCode() + ".");
            System.out.println("Response body: " + response.getBody());
            System.out.println("Customer has been deleted and can not be found.");
        }

        @Test
        void invalidCustomerId404() {
//            Given: a valid auth header and invalid customer id
            int customerId = 85;
            String token = encryptedToken();
            Assertions.assertNotNull(token);
            System.out.println(token);

//           build authheader
            HttpHeaders authHeader = new HttpHeaders();
            authHeader.setContentType(MediaType.APPLICATION_JSON);
            authHeader.setBearerAuth(token);
            HttpEntity<String> headerEntity = new HttpEntity<>(authHeader);
            System.out.println(headerEntity);

//        When: a successful connection is made
            String uri=
                    String.format("%s/%d", getBaseUriForDeleteCustomer(), customerId);
            System.out.println(uri);

            ResponseEntity<Map<String, Object>> response =
                    getRestTemplate().exchange(uri, HttpMethod.DELETE, headerEntity, new ParameterizedTypeReference<>() {
                    });
            System.out.println("Response body: " + response.getBody());

            //          Then: a 404 NOT FOUND response is returned
            Assertions.assertSame(HttpStatus.NOT_FOUND, response.getStatusCode());
            System.out.println("Response code is " + response.getStatusCode() + ".");
            System.out.println("Response body: " + response.getBody());
            //        And: the error message contains
            Map<String, Object> error = response.getBody();
            assert error != null;
            Assertions.assertEquals(error.get("status code").toString().substring(0,3), HttpStatus.NOT_FOUND.toString().substring(0,3));
            Assertions.assertTrue(error.containsValue("/api/owners-tools/customers/delete-customer/85"));
            Assertions.assertTrue(error.containsKey("message"));
            Assertions.assertTrue(error.containsKey("timestamp"));
            System.out.println("Negative test case complete for delete customer by id.");
        }
    }
}

