package com.restaubot.spring.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.restaubot.spring.models.dto.CustomerDTO;
import com.restaubot.spring.models.entities.CustomerEntity;
import com.restaubot.spring.repositories.CustomerRepository;
import com.restaubot.spring.security.CustomRuntimeException;

@Service
@Transactional
public class CustomerService {

    private static final Logger logger = LogManager.getLogger(CustomerService.class);

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ModelMapper modelMapper;

    public CustomerService(CustomerRepository customerRepository2) {
    }

    public List<CustomerDTO> listAllCustomers() throws CustomRuntimeException {
        try {
            return customerRepository.findAll().stream()
                    .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error listing all customers:", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }
    }

    public CustomerDTO getCustomerById(Integer id) throws CustomRuntimeException {
        Optional<CustomerEntity> optionalCustomer = Optional.empty();
        try {
            optionalCustomer = customerRepository.findById(id);
        } catch (Exception e) {
            logger.error("Error findById", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }
        if (optionalCustomer.isEmpty()) {
            throw new CustomRuntimeException(CustomRuntimeException.CUSTOMER_NOT_FOUND);
        }
        return modelMapper.map(optionalCustomer.get(), CustomerDTO.class);
    }

    public CustomerDTO getCustomerByMail(CustomerDTO customerDTO) throws CustomRuntimeException {
        Optional<CustomerEntity> optionalCustomer = Optional.empty();
        try {
            optionalCustomer = customerRepository.findByMail(customerDTO.getMail());
        } catch (Exception e) {
            logger.error("Error findByLogin", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }
        if (optionalCustomer.isEmpty()) {
            throw new CustomRuntimeException(CustomRuntimeException.CUSTOMER_NOT_FOUND);
        }
        return modelMapper.map(optionalCustomer.get(), CustomerDTO.class);
    }

    public CustomerDTO saveCustomer(CustomerDTO customer) throws CustomRuntimeException {
        CustomerEntity customerEntity = modelMapper.map(customer, CustomerEntity.class);

        /*
         * if (customerEntity.getIdCustomer() != null){
         * logger.error("Customer id should be null");
         * throw new
         * CustomRuntimeException(CustomRuntimeException.ID_CUSTOMER_SHOULD_BE_NULL);
         * }
         */

        CustomerEntity response = null;
        try {
            response = customerRepository.save(customerEntity);
        } catch (Exception e) {
            logger.error("Error saving customer:", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }

        return modelMapper.map(response, CustomerDTO.class);
    }

    public CustomerDTO updateCustomer(CustomerDTO customer) throws CustomRuntimeException {
        CustomerEntity customerEntity = modelMapper.map(customer, CustomerEntity.class);

        Optional<CustomerEntity> optionalCustomer = customerRepository.findById(customerEntity.getIdCustomer());
        if (optionalCustomer.isEmpty()) {
            throw new CustomRuntimeException(CustomRuntimeException.CUSTOMER_NOT_FOUND);
        }

        CustomerEntity response = null;
        try {
            response = customerRepository.save(customerEntity);
        } catch (Exception e) {
            logger.error("Error updating customer:", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }

        return modelMapper.map(response, CustomerDTO.class);
    }

    public void deleteCustomerById(Integer id) throws CustomRuntimeException {
        customerRepository.deleteById(id);
    }

    public CustomerDTO createCustomer(CustomerDTO customerDTO) throws CustomRuntimeException {
        Optional<CustomerEntity> optionalCustomer = Optional.empty();
        String password = customerDTO.getPassword();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String bCrypPassword = bCryptPasswordEncoder.encode(password);
        CustomerDTO customer = new CustomerDTO(customerDTO.getSurname(), customerDTO.getFirstname(),
                    customerDTO.getMail(), customerDTO.getPhone(), customerDTO.getAddress(), bCrypPassword, customerDTO.getRole());
        try {
            optionalCustomer = customerRepository.findByMail(customerDTO.getMail());
        } catch (Exception e) {
            logger.error("Error findByLogin", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }
        if (optionalCustomer.isEmpty()) {
            saveCustomer(customer);
        }else{
            throw new CustomRuntimeException(CustomRuntimeException.MAIL_TAKEN);
        }
        return customer;
    }

}
