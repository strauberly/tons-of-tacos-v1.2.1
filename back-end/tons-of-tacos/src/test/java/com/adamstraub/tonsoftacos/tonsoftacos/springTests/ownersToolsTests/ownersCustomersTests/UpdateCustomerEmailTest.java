package com.adamstraub.tonsoftacos.tonsoftacos.springTests.ownersToolsTests.ownersCustomersTests;

import com.adamstraub.tonsoftacos.dto.businessDto.CustomerReturnedToOwner;
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

public class UpdateCustomerEmailTest {



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
        void updateCustomerEmail200() {
            //            get valid token
            String token = encryptedToken();
            Assertions.assertNotNull(token);

//            Given: a valid query, auth header and an email.
            String customerUid = "gd34-igjr";
            String newCustomerEmail = "larry@larbo.com";
            String parameter = "customerUid";
//              build valid auth header
            HttpHeaders authHeader = new HttpHeaders();
            authHeader.setContentType(MediaType.APPLICATION_JSON);
            authHeader.setBearerAuth(token);
            HttpEntity<String> headersEntity = new HttpEntity<>(authHeader);

//             get email before alteration
            String getCustomerUri =
                    String.format("%s?%s=%s", getBaseUriForGetCustomerByUid(), parameter, customerUid);

            System.out.println(getCustomerUri);

            ResponseEntity<CustomerReturnedToOwner> getCustomerResponse =
                    getRestTemplate().exchange(getCustomerUri, HttpMethod.GET, headersEntity, new ParameterizedTypeReference<>() {
                    });
            System.out.println("Customer before email change: " + getCustomerResponse.getBody());

//            When: a successful connection is made
            String uri =
                    String.format("%s/%s/%s", getBaseUriForUpdateEmail(), customerUid, newCustomerEmail);
            System.out.println(uri);
//
            ResponseEntity<String> response =
                    getRestTemplate().exchange(uri, HttpMethod.PUT, headersEntity,
                            new ParameterizedTypeReference<>() {
                            });

            //            Then: a response of 200 returned
            Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
            System.out.println("Response code: " + response.getStatusCode());
            System.out.println("Response body: " + response.getBody());

            String getCustomerUri2 =
                    String.format("%s?%s=%s", getBaseUriForGetCustomerByUid(), parameter, customerUid);

            System.out.println(getCustomerUri2);

            ResponseEntity<CustomerReturnedToOwner> getCustomerResponse2 =
                    getRestTemplate().exchange(getCustomerUri2, HttpMethod.GET, headersEntity,
                            new ParameterizedTypeReference<>() {
                            });
            System.out.println("Customer after email change: " + getCustomerResponse2.getBody());
            Assertions.assertNotEquals(getCustomerResponse.getBody().getEmail(),
                    getCustomerResponse2.getBody().getEmail());

        }

        @Test
        void customerEmailNotUpdatedInvalidEmail400() {
            //            get valid token
//                String token = validToken();
            String token = encryptedToken();
            Assertions.assertNotNull(token);

//            Given: a valid id, valid auth header and invalid email.
            String customerUid = "gd34-igjr";
            String newCustomerEmail = "larrylarbo.com";

//            build valid auth header
            HttpHeaders authHeader = new HttpHeaders();
            authHeader.setContentType(MediaType.APPLICATION_JSON);
            authHeader.setBearerAuth(token);
            HttpEntity<String> headersEntity = new HttpEntity<>(authHeader);

//            When: a successful connection is made
            String uri =
                    String.format("%s/%s/%s", getBaseUriForUpdateEmail(), customerUid, newCustomerEmail);
            System.out.println(uri);
//
            ResponseEntity<Map<String, Object>> response =
                    getRestTemplate().exchange(uri, HttpMethod.PUT, headersEntity,
                            new ParameterizedTypeReference<>() {
                            });

            //            Then: a response of 404 returned
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            System.out.println("Response code is " + response.getStatusCode() + ".");
            System.out.println("Response body: " + response.getBody());

//                And: the error message contains
            Map<String, Object> error = response.getBody();
            assert error != null;
            Assertions.assertEquals(error.get("status code").toString().substring(0,3), HttpStatus.BAD_REQUEST.toString().substring(0,3));
            Assertions.assertTrue(error.containsValue("/api/owners-tools/customers/edit-customer-email/gd34-igjr/larrylarbo.com"));
            Assertions.assertTrue(error.containsKey("message"));
            Assertions.assertTrue(error.containsKey("timestamp"));
            System.out.println("Negative test case complete for changing the email of an existing customer.");
        }

        @Test
        void customerEmailNotUpdatedNoSuchCustomer404() {
            String token = encryptedToken();
            Assertions.assertNotNull(token);

//            Given: an invalid id, valid auth header and valid email.
            int customerUid = 13;
            String newCustomerEmail = "larry@larbo.com";

//              build valid auth header
            HttpHeaders authHeader = new HttpHeaders();
            authHeader.setContentType(MediaType.APPLICATION_JSON);
            authHeader.setBearerAuth(token);
            HttpEntity<String> headersEntity = new HttpEntity<>(authHeader);

//            When: a successful connection is made
            String uri =
                    String.format("%s/%d/%s", getBaseUriForUpdateEmail(), customerUid, newCustomerEmail);
            System.out.println(uri);
//
            ResponseEntity<Map<String, Object>> response =
                    getRestTemplate().exchange(uri, HttpMethod.PUT, headersEntity,
                            new ParameterizedTypeReference<>() {
                            });

            //            Then: a response of 404 returned
            Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            System.out.println("Response code is " + response.getStatusCode() + ".");
            System.out.println("Response body: " + response.getBody());

//                And: the error message contains
            Map<String, Object> error = response.getBody();
            assert error != null;
            Assertions.assertEquals(error.get("status code").toString().substring(0,3), HttpStatus.NOT_FOUND.toString().substring(0,3));
            Assertions.assertTrue(error.containsValue("/api/owners-tools/customers/edit-customer-email/13/larry@larbo.com"));
            Assertions.assertTrue(error.containsKey("message"));
            Assertions.assertTrue(error.containsKey("timestamp"));
            System.out.println("Negative test case complete for changing the email of an existing customer.");
        }
    }
}

