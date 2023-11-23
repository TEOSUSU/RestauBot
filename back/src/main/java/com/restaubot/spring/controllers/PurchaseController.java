package com.restaubot.spring.controllers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restaubot.spring.models.dto.PurchaseDTO;
import com.restaubot.spring.services.PurchaseService;
import com.restaubot.spring.services.RestaurantService;
import com.restaubot.spring.services.TypeService;
import org.springframework.web.bind.annotation.GetMapping;
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
}
