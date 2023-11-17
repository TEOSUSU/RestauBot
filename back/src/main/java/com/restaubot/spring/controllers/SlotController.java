package com.restaubot.spring.controllers;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restaubot.spring.models.dto.SlotDTO;
import com.restaubot.spring.security.CustomRuntimeException;
import com.restaubot.spring.services.SlotService;

@RestController
@RequestMapping("/api/slot")
public class SlotController {
    private static final Logger logger = LogManager.getLogger(RestaurantController.class);
    private static final String UNEXPECTED_EXCEPTION = "Unexpected Exception : {}";

    @Autowired
    SlotService slotService;

    @PostMapping("")
    public ResponseEntity<SlotDTO> update(@RequestBody SlotDTO slotDto) {
        logger.info("Process request : create a time slot : {}");

        try {
            SlotDTO createdSlot = slotService.createSlot(slotDto);
            return new ResponseEntity<>(createdSlot,HttpStatus.CREATED);
        } catch (CustomRuntimeException e) {
            if (e.getMessage().equals(CustomRuntimeException.SLOT_NOT_FOUND)) {
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
