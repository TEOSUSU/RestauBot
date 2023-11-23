package com.restaubot.spring.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.restaubot.spring.models.dto.CustomerDTO;
import com.restaubot.spring.security.CustomRuntimeException;
import com.restaubot.spring.services.CustomerService;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Test
    public void testCreateCustomer_Success() throws Exception {
        CustomerDTO customerDTO = new CustomerDTO("John", "Doe", "john.doe@example.com", "123456789", "123 Main St",
                "password");

        when(customerService.createCustomer(any(CustomerDTO.class))).thenReturn(customerDTO);

        String customerJson = "{\"surname\":\"John\",\"firstname\":\"Doe\",\"mail\":\"john.doe@example.com\",\"phone\":\"123456789\",\"address\":\"123 Main St\",\"password\":\"password\"}";

        mockMvc.perform(post("/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerJson))
                .andExpect(status().isOk());
        // Ajoutez davantage d'assertions en fonction de vos besoins.
    }

    @Test
    public void testCreateCustomer_Conflict() throws Exception {
        CustomerDTO customerDTO = new CustomerDTO("John", "Doe", "john.doe@example.com", "123456789", "123 Main St", "password");

        when(customerService.createCustomer(any(CustomerDTO.class)).thenThrow(new CustomRuntimeException(CustomRuntimeException.MAIL_TAKEN)));

        String customerJson = "{\"surname\":\"John\",\"firstname\":\"Doe\",\"mail\":\"john.doe@example.com\",\"phone\":\"123456789\",\"address\":\"123 Main St\",\"password\":\"password\"}";

        mockMvc.perform(post("/create")
            .contentType(MediaType.APPLICATION_JSON)
            .content(customerJson))
            .andExpect(status().isConflict());
        // Ajoutez davantage d'assertions en fonction de vos besoins.
    }
}
