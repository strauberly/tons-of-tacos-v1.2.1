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

public class UpdateCustomerPhoneTest {


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
        void updateCustomerPhone200() {
            //            get valid token
            String token = encryptedToken();
            Assertions.assertNotNull(token);

//            Given: a valid customer Uid, phone number and valid auth header
            String customerUid = "gd34-igjr";
            String newCustomerPhone = "555.555.5155";
            String parameter = "customerUid";

//              build valid auth header
            HttpHeaders authHeader = new HttpHeaders();
            authHeader.setContentType(MediaType.APPLICATION_JSON);
            authHeader.setBearerAuth(token);
            HttpEntity<String> headersEntity = new HttpEntity<>(authHeader);


//              get customer before alteration
            String getCustomerUri =
                    String.format("%s?%s=%s", getBaseUriForGetCustomerByUid(),parameter, customerUid);
            System.out.println(getCustomerUri);
//            call customer before alteration
            ResponseEntity<CustomerReturnedToOwner> getCustomerResponse =
                    getRestTemplate().exchange(getCustomerUri, HttpMethod.GET, headersEntity, new ParameterizedTypeReference<>() {
                    });
            System.out.println("Customer before alteration: " + getCustomerResponse.getBody());

//            When: a successful connection is made
            String uri =
                    String.format("%s/%s/%s",  getBaseUriForUpdatePhone(), customerUid, newCustomerPhone);
            System.out.println(uri);
//
            ResponseEntity<String> response =
                    getRestTemplate().exchange(uri, HttpMethod.PUT, headersEntity,
                            new ParameterizedTypeReference<>() {});

            //            Then: a response of 200 returned
            Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
            System.out.println("Response code: " + response.getStatusCode());
            System.out.println("Response body: " + response.getBody());

//            And: when the customer is called the phone number should not be the same.

            String getCustomerUri2 =
                    String.format("%s?%s=%s", getBaseUriForGetCustomerByUid(), parameter, customerUid);
            System.out.println(getCustomerUri2);

            ResponseEntity<CustomerReturnedToOwner> getCustomerResponse2 =
                    getRestTemplate().exchange(getCustomerUri2, HttpMethod.GET, headersEntity,
                            new ParameterizedTypeReference<>() {
                            });
            Assertions.assertNotEquals(getCustomerResponse.getBody().getPhone(),
                    getCustomerResponse2.getBody().getPhone());
            Assertions.assertEquals(getCustomerResponse.getBody().getCustomerUid(), getCustomerResponse2.getBody().getCustomerUid());
            System.out.println("Customer after alteration: " + getCustomerResponse2.getBody());
            System.out.println("Successful test case for changing customer phone number complete. The customer id is the same and the phone number is not." );

        }

        @Test
        void customerPhoneNotUpdatedNoSuchCustomer404() {
            //            get valid token
//                String token = validToken();
            String token = encryptedToken();
            Assertions.assertNotNull(token);

//            Given: an invalid customer id, valid phone number and valid auth header
            int customerId = 14;
            String newCustomerPhone = "555.555.5156";
            String parameter = "customerId";
//             get order before alteration

            HttpHeaders authHeader = new HttpHeaders();
            authHeader.setContentType(MediaType.APPLICATION_JSON);
            authHeader.setBearerAuth(token);
            HttpEntity<String> headersEntity = new HttpEntity<>(authHeader);

//            When: a successful connection is made
            String uri =
                    String.format("%s/%d/%s",  getBaseUriForUpdatePhone(), customerId, newCustomerPhone);
            System.out.println(uri);
//
            ResponseEntity<Map<String, Object>> response =
                    getRestTemplate().exchange(uri, HttpMethod.PUT, headersEntity,
                            new ParameterizedTypeReference<>() {});

//            Then: a response of 404 returned
            Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            System.out.println("Response code is " + response.getStatusCode() + ".");
            System.out.println(response.getBody());
            System.out.println(response.getBody().get("uri").toString());

//            And: the error message contains
            Map<String, Object> error = response.getBody();
            assert error != null;
            Assertions.assertEquals(error.get("status code").toString().substring(0,3), HttpStatus.NOT_FOUND.toString().substring(0,3));
            Assertions.assertTrue(error.containsValue("/api/owners-tools/customers/edit-customer-phone/14/555.555.5156"));
            Assertions.assertTrue(error.containsKey("message"));
            Assertions.assertTrue(error.containsKey("timestamp"));
            System.out.println("Negative test case complete for changing the name of an existing customer.");


        }
    }
}

