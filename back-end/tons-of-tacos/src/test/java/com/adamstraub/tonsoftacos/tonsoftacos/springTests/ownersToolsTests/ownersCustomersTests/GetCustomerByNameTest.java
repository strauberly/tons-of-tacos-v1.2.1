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
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GetCustomerByNameTest {

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
        void getCustomerByName200() {
//            Given: a valid customer name and valid auth header
            String token = encryptedToken();
            Assertions.assertNotNull(token);

            String parameter = "name";
            String customerName = "Tim Timson";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);
            HttpEntity<String> headersEntity = new HttpEntity<>(headers);

//            When: a connection is made
            String uri=
                    String.format("%s?%s=%s", getBaseUriForGetCustomerByName(), parameter, customerName );
            System.out.println(uri);
            ResponseEntity<CustomerReturnedToOwner> response =
                    getRestTemplate().exchange(uri, HttpMethod.GET, headersEntity, new ParameterizedTypeReference<>() {
                    });
            System.out.println("Customer returned from db: " + Objects.requireNonNull(response.getBody()).getName());

//            Then: a customer is returned with a name matching the name queried and a 200 response code
            Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
            System.out.println("Response code is " + response.getStatusCode() + ".");
            Assertions.assertEquals(customerName, Objects.requireNonNull(response.getBody()).getName());
            System.out.println("Customer name matches customer name queried: " + response.getBody().getName());
            Assertions.assertEquals(customerName, Objects.requireNonNull(response.getBody()).getName());
        }

        @Test
        void invalidCustomerName404() {
//            Given: an ivalid customer name and valid auth header
            String token = encryptedToken();
            Assertions.assertNotNull(token);

            String parameter = "name";
            String invalidCustomerName = "sdfkjh@#D";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);
            HttpEntity<String> headersEntity = new HttpEntity<>(headers);

//            When: a connection is made
            String uri=
                    String.format("%s?%s=%s", getBaseUriForGetCustomerByName(), parameter, invalidCustomerName );
            System.out.println(uri);
            ResponseEntity<Map<String, Object>> response =
                    getRestTemplate().exchange(uri, HttpMethod.GET, headersEntity, new ParameterizedTypeReference<>() {
                    });

            System.out.println("Response body: " + response.getBody());

//            Then: 404 is returned
            Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            System.out.println("Response code is " + response.getStatusCode() + ".");
            System.out.println(response);

            //      And: the error message contains
            Map<String, Object> error = response.getBody();
            assert error != null;
            Assertions.assertEquals(error.get("status code").toString().substring(0,3), HttpStatus.NOT_FOUND.toString().substring(0,3));
            Assertions.assertTrue(error.containsValue("/api/owners-tools/customers/get-customer-name/name"));
            Assertions.assertTrue(error.containsKey("message"));
            Assertions.assertTrue(error.containsKey("timestamp"));
        }
    }
}

