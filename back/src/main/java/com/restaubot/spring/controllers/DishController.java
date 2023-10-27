package com.restaubot.spring.controllers;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.restaubot.spring.models.dto.DishDTO;
import com.restaubot.spring.security.DishRuntimeException;
import com.restaubot.spring.services.DishService;

@RestController
@RequestMapping("/api/dishes")
public class DishController {
    
    private static final Logger logger = LogManager.getLogger(DishController.class);
    private static final String UNEXPECTED_EXCEPTION = "Unexpected Exception : {}";

    @Autowired
    DishService dishService;

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> create(@ModelAttribute DishDTO dishDto,  @RequestPart("file") MultipartFile file)
    throws IOException {
        logger.info("Process request : Create Dish");
        try {
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
}
