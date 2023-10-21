package com.restaubot.spring.controllers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restaubot.spring.models.dto.RestaurantDTO;
import com.restaubot.spring.security.CustomRuntimeException;
import com.restaubot.spring.services.RestaurantService;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {

    private static final Logger logger = LogManager.getLogger(RestaurantController.class);
    private static final String UNEXPECTED_EXCEPTION = "Unexpected Exception : {}";

    @Autowired
    RestaurantService restaurantService;

    @GetMapping("")
    public ResponseEntity<List<RestaurantDTO>> list() {
        logger.info("Process request : List all restaurants");
        try {
            List<RestaurantDTO> restaurant = restaurantService.listAllRestaurants();
            return new ResponseEntity<>(restaurant, HttpStatus.OK);
        } catch (CustomRuntimeException e) {
            if (e.getMessage().equals(CustomRuntimeException.SERVICE_ERROR)) {
                logger.warn(e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            logger.error(UNEXPECTED_EXCEPTION, e.getMessage());
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
    }

     @PostMapping("")
    public ResponseEntity<HttpStatus> update(@RequestBody RestaurantDTO restaurantDto) {
        logger.info("Process request : create restaurant : {}");
             System.out.println("Bonjour, monde !");

        try {
            restaurantService.createRestaurant(restaurantDto);
            return new ResponseEntity<>(HttpStatus.OK);
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
    
}
