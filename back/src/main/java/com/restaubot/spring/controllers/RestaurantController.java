package com.restaubot.spring.controllers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restaubot.spring.models.dto.RestaurantDTO;
import com.restaubot.spring.models.entities.RestaurantEntity;
import com.restaubot.spring.security.CustomRuntimeException;
import com.restaubot.spring.security.JwtTokenUtil;
import com.restaubot.spring.security.UserResponse;
import com.restaubot.spring.services.RestaurantService;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {

    private static final Logger logger = LogManager.getLogger(RestaurantController.class);
    private static final String UNEXPECTED_EXCEPTION = "Unexpected Exception : {}";

    @Autowired
    RestaurantService restaurantService;

     @Autowired
    JwtTokenUtil jwtTokenUtil;

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

    @GetMapping("/{mail}")
    public ResponseEntity<RestaurantDTO> getRestaurantByMail(@PathVariable String mail) {
        logger.info("Process request : Get restaurant by mail : {}", mail);
        try {
            RestaurantDTO restaurant = restaurantService.getRestaurantByMail(mail);
            return new ResponseEntity<>(restaurant, HttpStatus.OK);
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

    @GetMapping("/id/{id}")
    public ResponseEntity<RestaurantDTO> getRestaurantById(@PathVariable Integer id) {
        logger.info("Process request : Get restaurant by id : {}", id);
        try {
            RestaurantDTO restaurant = restaurantService.getRestaurantById(id);
            return new ResponseEntity<>(restaurant, HttpStatus.OK);
        } catch (CustomRuntimeException e) {
            if (e.getMessage().equals(CustomRuntimeException.RESTAURANT_NOT_FOUND)) {
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
    public ResponseEntity<RestaurantDTO> createRestaurant(@RequestBody RestaurantDTO restaurantDto) {
        logger.info("Process request: create restaurant");
        try {
            RestaurantDTO createdRestaurant = restaurantService.createRestaurant(restaurantDto);
            return new ResponseEntity<>(createdRestaurant, HttpStatus.CREATED);
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


    @PutMapping("/{restaurantId}/{slotId}")
    public RestaurantEntity assignRestaurantToSlot(
        @PathVariable Integer restaurantId,
        @PathVariable Integer slotId
    ){
        return restaurantService.assignRestaurantToSlot(restaurantId,slotId);
    }

    @PostMapping("/connexion")
    public ResponseEntity<?> getRoleRestaurantInToken(
            @RequestHeader(name = "Authorization") String token) {
        token = token.substring(7);
        Claims claims = jwtTokenUtil.parseClaims(token);
        String roleRestaurant = claims.get("role").toString().substring(0);
        System.out.println(roleRestaurant);
        UserResponse response = new UserResponse(roleRestaurant);
        return ResponseEntity.ok().body(response);
    }
}
