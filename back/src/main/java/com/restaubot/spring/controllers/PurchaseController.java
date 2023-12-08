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

import com.restaubot.spring.models.dto.CustomerDTO;
import com.restaubot.spring.models.dto.PurchaseDTO;
import com.restaubot.spring.services.PurchaseService;
import com.restaubot.spring.services.RestaurantService;
import com.restaubot.spring.services.TypeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.restaubot.spring.security.CustomRuntimeException;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {
    
    private static final Logger logger = LogManager.getLogger(PurchaseController.class);
    private static final String UNEXPECTED_EXCEPTION = "Unexpected Exception : {}";

    @Autowired
    PurchaseService purchaseService;
    @Autowired
    RestaurantService restaurantService;
    @Autowired
    TypeService typeService;

    @GetMapping("")
    public ResponseEntity<List<PurchaseDTO>> list() {
        logger.info("Process request : List all purchases");
        try {
            List<PurchaseDTO> purchasees = purchaseService.listAllPurchases();
            return new ResponseEntity<>(purchasees, HttpStatus.OK);
        } catch (CustomRuntimeException e) {
            if (e.getMessage().equals(CustomRuntimeException.SERVICE_ERROR)) {
                logger.warn(e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            logger.error(UNEXPECTED_EXCEPTION, e.getMessage());
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }        
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseDTO> get(@PathVariable Integer id) {
        logger.info("Process request : Get purchase by id : {}", id);
        try {
            PurchaseDTO customer = purchaseService.getPurchaseById(id);
            return new ResponseEntity<>(customer, HttpStatus.OK);
        } catch (CustomRuntimeException e) {
            if (e.getMessage().equals(CustomRuntimeException.CUSTOMER_NOT_FOUND)) {
                logger.warn(e.getMessage());
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            if (e.getMessage().equals(CustomRuntimeException.SERVICE_ERROR)) {
                logger.warn(e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            logger.error(UNEXPECTED_EXCEPTION, e.getMessage());
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<PurchaseDTO> createPurchase(@RequestBody PurchaseDTO purchaseDTO) {
        logger.info("Process request : Create purchase");
        try {
            PurchaseDTO createdPurchase = purchaseService.createPurchase(purchaseDTO);
            return new ResponseEntity<>(createdPurchase, HttpStatus.CREATED);
        } catch (CustomRuntimeException e) {
            if (e.getMessage().equals(CustomRuntimeException.SERVICE_ERROR)) {
                logger.warn(e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            logger.error(UNEXPECTED_EXCEPTION, e.getMessage());
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
    }

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

    @GetMapping("/restaurant/{restaurantId}")
    //pour récupérer les commandes faites par un client
    public ResponseEntity<List<PurchaseDTO>> listPurchasesByRestaurant(
            @PathVariable Integer restaurantId) {
        logger.info("Process request: List purchases for restaurant {}", restaurantId);
        try {
            List<PurchaseDTO> purchases = purchaseService.getPurchasesByRestaurant(restaurantId);
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
