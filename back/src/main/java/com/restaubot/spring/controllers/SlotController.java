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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restaubot.spring.models.dto.RestaurantDTO;
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

   @GetMapping("/{id}")
    public ResponseEntity<List<SlotDTO>> getSlotsByIdRestaurant(@PathVariable int id) throws CustomRuntimeException {
        logger.info("Process request : Get slot by id : {}", id);
        List<SlotDTO> slots = slotService.getSlotsByIdRestaurant(id);
        return new ResponseEntity<>(slots, HttpStatus.OK);
    }
    
}
