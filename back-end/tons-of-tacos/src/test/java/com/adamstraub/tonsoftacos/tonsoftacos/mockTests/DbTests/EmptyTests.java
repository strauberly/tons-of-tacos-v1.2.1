package com.adamstraub.tonsoftacos.tonsoftacos.mockTests.DbTests;

import com.adamstraub.tonsoftacos.dao.CustomerRepository;
import com.adamstraub.tonsoftacos.dao.OrdersRepository;
import com.adamstraub.tonsoftacos.tonsoftacos.testSupport.ownersToolsSupport.OwnersToolsTestsSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmptyTests extends OwnersToolsTestsSupport {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrdersRepository ordersRepository;

    @MockBean
    private CustomerRepository customerRepository;

    @Test
    void emptyOrdersDBTest404() throws Exception{
        when(ordersRepository.findAll()).thenReturn(null);

        RequestBuilder request = MockMvcRequestBuilders
                .get(getBaseUriForGetAllOrders())
                .header("Authorization", "Bearer " + buildGoodToken())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request).andExpect(status().isNotFound()).andReturn();

        System.out.println(result.getResponse().getStatus());
        System.out.println(result.getResponse().getContentAsString());
        System.out.println("Negative test case successful. Exception handler functioning.");
    }

    @Test
    void emptyCustomerDBTest404() throws Exception{
        when(customerRepository.findAll()).thenReturn(null);

        RequestBuilder request = MockMvcRequestBuilders
                .get(getBaseUriForGetAllCustomers())
                .header("Authorization", "Bearer " + buildGoodToken())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request).andExpect(status().isNotFound()).andReturn();

        System.out.println(result.getResponse().getStatus());
        System.out.println(result.getResponse().getContentAsString());
        System.out.println("Negative test case successful. Exception handler functioning.");
    }


}
