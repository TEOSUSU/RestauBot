package com.restaubot.spring.controllers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restaubot.spring.models.dto.PurchaseDTO;
import com.restaubot.spring.security.CustomRuntimeException;

import com.restaubot.spring.services.PurchaseService;


@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    private static final Logger logger = LogManager.getLogger(CustomerController.class);
    private static final String UNEXPECTED_EXCEPTION = "Unexpected Exception : {}";


    @GetMapping("/customer/{customerId}/restaurant/{restaurantId}") 
    //pour récupérer les commandes faites par un client dans un restaurant
    public ResponseEntity<List<PurchaseDTO>> listPurchasesByCustomerAndRestaurant(
            @PathVariable Integer customerId, @PathVariable Integer restaurantId) {
        logger.info("Process request: List purchases for customer {} in restaurant {}", customerId, restaurantId);
        try {
            List<PurchaseDTO> purchases = purchaseService.getPurchasesByCustomerAndRestaurant(customerId, restaurantId);
            return new ResponseEntity<>(purchases, HttpStatus.OK);
        } catch (CustomRuntimeException e) {
            if (e.getMessage().equals(CustomRuntimeException.SERVICE_ERROR)) {
                logger.warn(e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            logger.error(UNEXPECTED_EXCEPTION, e.getMessage());
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
    }

    @GetMapping("/customer/{customerId}")
    //pour récupérer les commandes faites par un client
    public ResponseEntity<List<PurchaseDTO>> listPurchasesByCustomer(
            @PathVariable Integer customerId) {
        logger.info("Process request: List purchases for customer {}", customerId);
        try {
            List<PurchaseDTO> purchases = purchaseService.getPurchasesByCustomer(customerId);
            return new ResponseEntity<>(purchases, HttpStatus.OK);
        } catch (CustomRuntimeException e) {
            if (e.getMessage().equals(CustomRuntimeException.SERVICE_ERROR)) {
                logger.warn(e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            logger.error(UNEXPECTED_EXCEPTION, e.getMessage());
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
    }


  
}
