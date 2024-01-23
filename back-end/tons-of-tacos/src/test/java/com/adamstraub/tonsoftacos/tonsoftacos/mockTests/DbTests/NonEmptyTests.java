package com.adamstraub.tonsoftacos.tonsoftacos.mockTests.DbTests;

        import com.adamstraub.tonsoftacos.tonsoftacos.testSupport.ownersToolsSupport.OwnersToolsTestsSupport;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;


        import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
        import org.springframework.boot.test.context.SpringBootTest;
        import org.springframework.http.MediaType;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

        import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
    @TestPropertySource("classpath:test-application.properties")
@Sql(scripts = {
        "classpath:/test-schema.sql",
        "classpath:/test-data.sql",
},
        config = @SqlConfig(encoding = "utf-8"))

public class NonEmptyTests extends OwnersToolsTestsSupport{
    @Autowired
    private MockMvc mockMvc;


    @Test
        void nonEmptyOrdersDBTest200() throws Exception {
                RequestBuilder request = MockMvcRequestBuilders
                .get(getBaseUriForGetAllOrders())
                .header("Authorization", "Bearer " + buildGoodToken())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assertions.assertNotNull(result.getResponse().getContentAsString());
        System.out.println(result.getResponse().getStatus());
        System.out.println(result.getResponse().getContentAsString());
        System.out.println("All orders returned successfully.");
    }

    @Test
    void nonEmptyCustomersDBTest200() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get(getBaseUriForGetAllCustomers())
                .header("Authorization", "Bearer " + buildGoodToken())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assertions.assertNotNull(result.getResponse().getContentAsString());
        System.out.println(result.getResponse().getStatus());
        System.out.println(result.getResponse().getContentAsString());
        System.out.println("All customers returned successfully.");
    }
}