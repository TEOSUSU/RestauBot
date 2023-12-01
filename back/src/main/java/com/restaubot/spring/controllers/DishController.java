package com.restaubot.spring.controllers;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.restaubot.spring.models.dto.DishDTO;
import com.restaubot.spring.models.dto.MenuDTO;
import com.restaubot.spring.security.DishRuntimeException;
import com.restaubot.spring.security.MenuRunTimeException;
import com.restaubot.spring.security.RestaurantRuntimeException;
import com.restaubot.spring.security.TypeRuntimeException;
import com.restaubot.spring.services.DishService;
import com.restaubot.spring.services.RestaurantService;
import com.restaubot.spring.services.TypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restaubot.spring.models.dto.DishDTO;
import com.restaubot.spring.security.CustomRuntimeException;
import com.restaubot.spring.services.DishService;

@RestController
@RequestMapping("/api/dishes")
public class DishController {
    
    private static final Logger logger = LogManager.getLogger(DishController.class);
    private static final String UNEXPECTED_EXCEPTION = "Unexpected Exception : {}";

    @Autowired
    DishService dishService;
    @Autowired
    RestaurantService restaurantService;
    @Autowired
    TypeService typeService;

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> create(@ModelAttribute DishDTO dishDto, 
        @RequestParam("restaurantId") Integer restaurantId,
        @RequestParam("typeId") Integer typeId, @RequestParam("file") MultipartFile file) 
        throws IOException, CustomRuntimeException, TypeRuntimeException {
        logger.info("Process request : Create Dish");
        try {
            dishDto.setRestaurant(restaurantService.getRestaurantById(restaurantId));
            dishDto.setType(typeService.getTypeById(typeId));
            dishService.createDish(dishDto, file);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (DishRuntimeException e) {
            if (e.getMessage().equals(DishRuntimeException.SERVICE_ERROR)) {
                logger.warn(e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            logger.error(UNEXPECTED_EXCEPTION, e.getMessage());
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }        
    }

    @PostMapping("/modify/{dishId}")
    public ResponseEntity<HttpStatus> modify(@ModelAttribute DishDTO dishDto, 
    @RequestParam("file") MultipartFile file, @RequestParam("typeId") Integer typeId,
    @PathVariable Integer dishId) throws TypeRuntimeException {
        logger.info("Process request : Modify dish");
        try {
            dishService.modifyDish(dishDto, file, dishId, typeId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CustomRuntimeException e) {
            if (e.getMessage().equals(CustomRuntimeException.SERVICE_ERROR)) {
                logger.warn(e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            logger.error(UNEXPECTED_EXCEPTION, e.getMessage());
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }        
    }

    @PostMapping("/delete/{dishId}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Integer dishId) {
        logger.info("Process request : Delete dish");
        try {
            dishService.deleteDish(dishId);
            return new ResponseEntity<>(HttpStatus.OK);
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
    public ResponseEntity<DishDTO> getDishById(@PathVariable Integer id) {
        logger.info("Process request : List all dishes");
        try {
            DishDTO dishes = dishService.getDishById(id);
            return new ResponseEntity<>(dishes, HttpStatus.OK);
        } catch (CustomRuntimeException e) {
            if (e.getMessage().equals(CustomRuntimeException.SERVICE_ERROR)) {
                logger.warn(e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            logger.error(UNEXPECTED_EXCEPTION, e.getMessage());
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }        
    }

    @GetMapping("")
    public ResponseEntity<List<DishDTO>> list() {
        logger.info("Process request : List all dishes");
        try {
            List<DishDTO> dishes = dishService.listAllDishes();
            return new ResponseEntity<>(dishes, HttpStatus.OK);
        } catch (CustomRuntimeException e) {
            if (e.getMessage().equals(CustomRuntimeException.SERVICE_ERROR)) {
                logger.warn(e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            logger.error(UNEXPECTED_EXCEPTION, e.getMessage());
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }        
    }

    @GetMapping("/details/{purchaseId}")
    public ResponseEntity<List<DishDTO>> getDishDetails(@PathVariable Integer purchaseId) {
        logger.info("Process request : Get dish details by purchase id : {}", purchaseId);
        try {
            List<DishDTO> dishes = dishService.getDishDetails(purchaseId);
            return new ResponseEntity<>(dishes, HttpStatus.OK);
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

