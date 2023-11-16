package com.restaubot.spring.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restaubot.spring.models.dto.CustomerDTO;
import com.restaubot.spring.models.dto.PurchaseDTO;
import com.restaubot.spring.models.dto.RestaurantDTO;
import com.restaubot.spring.models.entities.CustomerEntity;
import com.restaubot.spring.models.entities.PurchaseEntity;
import com.restaubot.spring.repositories.CustomerRepository;
import com.restaubot.spring.repositories.PurchaseRepository;
import com.restaubot.spring.security.CustomRuntimeException;

@Service
@Transactional
public class PurchaseService {
     private static final Logger logger = LogManager.getLogger(CustomerService.class);

    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<PurchaseDTO> getPurchasesByCustomerAndRestaurant(Integer customerId, Integer restaurantId) throws CustomRuntimeException {
        try {
            List<PurchaseEntity> purchases = purchaseRepository.getPurchasesByCustomerIdAndRestaurantId(customerId, restaurantId);
            return purchases.stream()
                    .map(purchase -> modelMapper.map(purchase, PurchaseDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error retrieving purchases:", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }
    }

    public List<PurchaseDTO> getPurchasesByCustomer(Integer customerId) throws CustomRuntimeException {
        try {
            List<PurchaseEntity> purchases = purchaseRepository.getPurchasesByCustomerId(customerId);
            return purchases.stream()
                    .map(purchase -> modelMapper.map(purchase, PurchaseDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error retrieving purchases:", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }
    }
}