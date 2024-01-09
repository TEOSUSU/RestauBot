package com.restaubot.spring.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.restaubot.spring.models.dto.CustomerDTO;
import com.restaubot.spring.models.entities.CustomerEntity;
import com.restaubot.spring.repositories.CustomerRepository;
import com.restaubot.spring.security.CustomRuntimeException;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this); // Initialisation des mocks

        customerService = new CustomerService(customerRepository);
    }

    @Test
    public void testCreateCustomer_Success() throws CustomRuntimeException {
        CustomerDTO customerDTO = new CustomerDTO("John", "Doe", "john.doe@example.com", "123456789", "123 Main St",
                "password", "ROLE_CUSTOMER");

        when(customerRepository.findByMail(customerDTO.getMail())).thenReturn(Optional.empty());

        CustomerDTO createdCustomer = customerService.createCustomer(customerDTO);

        assertNotNull(createdCustomer);
        // Ajoutez davantage d'assertions en fonction de vos besoins.
    }

    @Test
    public void testCreateCustomer_EmailTaken() {
        CustomerDTO customerDTO = new CustomerDTO("John", "Doe", "john.doe@example.com", "123456789", "123 Main St",
                "password", "ROLE_CUSTOMER");

        when(customerRepository.findByMail(customerDTO.getMail())).thenReturn(Optional.of(new CustomerEntity()));

        assertThrows(CustomRuntimeException.class, () -> {
            customerService.createCustomer(customerDTO);
        });
        // Ajoutez davantage d'assertions en fonction de vos besoins.
    }
}
