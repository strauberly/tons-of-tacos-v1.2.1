package com.adamstraub.tonsoftacos.tonsoftacos.springTests.ownersToolsTests.ownersOrdersTests;

import com.adamstraub.tonsoftacos.dao.OrdersRepository;
import com.adamstraub.tonsoftacos.dto.businessDto.DailySales;
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

public class GetTodaysSalesTests {
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
        @Autowired
        OrdersRepository ordersRepository;

        @Test
        void todaysSalesReturned200() {

//            Given: one or more orders have been closed marking a sale and a valid authheader
            //            get valid token
            String token = encryptedToken();
            Assertions.assertNotNull(token);

//           build authheader
            HttpHeaders authHeader = new HttpHeaders();
            authHeader.setContentType(MediaType.APPLICATION_JSON);
            authHeader.setBearerAuth(token);
            HttpEntity<String> headerEntity = new HttpEntity<>(authHeader);


            String orderIdOne = "654654-465465-555";
            String orderIdTwo = "654654-4655-555";

//            mark order one ready
            String orderOneReadyUri =
                    String.format("%s/%s", getBaseUriForOrderReady(), orderIdOne);
            System.out.println(orderOneReadyUri);

            ResponseEntity<OrderReturnedToOwner> orderOneReadyResponse =
                    getRestTemplate().exchange(orderOneReadyUri, HttpMethod.PUT, headerEntity, new ParameterizedTypeReference<>() {
                    });

            System.out.println(orderOneReadyResponse.getStatusCode());
            System.out.println(orderOneReadyResponse.getBody());
            System.out.println("Order one ready: " + (!ordersRepository.findByOrderUid(orderIdOne).getReady().equals("no")));

            //            mark order one closed
            String closeOrderOneUri =
                    String.format("%s/%s", getBaseUriForCloseOrder(), orderIdOne);
            System.out.println(closeOrderOneUri);

            ResponseEntity<OrderReturnedToOwner> orderOneClosedResponse =
                    getRestTemplate().exchange(closeOrderOneUri, HttpMethod.PUT, headerEntity, new ParameterizedTypeReference<>() {
                    });
            System.out.println(orderOneClosedResponse.getStatusCode());
            System.out.println(orderOneClosedResponse.getBody());
            System.out.println("Order one closed: " + !ordersRepository.findByOrderUid(orderIdOne).getClosed().equals("no"));

            //            mark order two ready
            String orderTwoReadyUri =
                    String.format("%s/%s", getBaseUriForOrderReady(), orderIdTwo);
            System.out.println(orderOneReadyUri);

            ResponseEntity<OrderReturnedToOwner> orderTwoReadyResponse =
                    getRestTemplate().exchange(orderTwoReadyUri, HttpMethod.PUT, headerEntity, new ParameterizedTypeReference<>() {
                    });

            System.out.println(orderTwoReadyResponse.getStatusCode());
            System.out.println(orderTwoReadyResponse.getBody());
            System.out.println("Order two ready: " + (!ordersRepository.findByOrderUid(orderIdTwo).getReady().equals("no")));

            //            mark order two closed
            String closeOrderTwoUri =
                    String.format("%s/%s", getBaseUriForCloseOrder(), orderIdTwo);
            System.out.println(closeOrderTwoUri);

            ResponseEntity<OrderReturnedToOwner> orderTwoClosedResponse =
                    getRestTemplate().exchange(closeOrderTwoUri, HttpMethod.PUT, headerEntity, new ParameterizedTypeReference<>() {
                    });
            System.out.println(orderTwoClosedResponse.getStatusCode());
            System.out.println(orderTwoClosedResponse.getBody());
            System.out.println("Order two closed: " + !ordersRepository.findByOrderUid(orderIdTwo).getClosed().equals("no"));


//            When:  a successful connection made
            String salesUri =
                    String.format("%s", getBaseUriForSales());
            System.out.println(salesUri);
            ResponseEntity<DailySales> response =
                    getRestTemplate().exchange(salesUri, HttpMethod.GET, headerEntity, new ParameterizedTypeReference<>() {
                    });
//            Then:  response code will be 200
            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
            System.out.println("Response code is " + response.getStatusCode() + ".");
            System.out.println(response.getBody());

//            And: the sum of the orders totals will equal the daily sales total
            Assertions.assertEquals(Objects.requireNonNull(orderOneReadyResponse.getBody()).getOrderTotal().add(Objects.requireNonNull(orderTwoReadyResponse.getBody()).getOrderTotal()), Objects.requireNonNull(response.getBody()).getTotal());

            System.out.println("Total of order 1 and order 2 equals daily sales total: " + (orderOneReadyResponse.getBody().getOrderTotal().add(orderTwoReadyResponse.getBody().getOrderTotal()).equals(response.getBody().getTotal())));
            System.out.println("Successful test case for daily sales complete.");
        }
    }

}

