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

import com.restaubot.spring.models.dto.TypeDTO;
import com.restaubot.spring.models.dto.TypeDTO;
import com.restaubot.spring.models.entities.TypeEntity;
import com.restaubot.spring.security.TypeRuntimeException;
import com.restaubot.spring.security.TypeRuntimeException;
import com.restaubot.spring.services.TypeService;

@RestController
@RequestMapping("/api/types")
public class TypeController {
    
    private static final Logger logger = LogManager.getLogger(TypeController.class);
    private static final String UNEXPECTED_EXCEPTION = "Unexpected Exception : {}";

    @Autowired
    TypeService typeService;

    @GetMapping("")
    public ResponseEntity<List<TypeDTO>> list() {
        logger.info("Process request : List all Types");
        try {
            List<TypeDTO> types = typeService.listAllTypes();
            return new ResponseEntity<>(types, HttpStatus.OK);
        } catch (TypeRuntimeException e) {
            if (e.getMessage().equals(TypeRuntimeException.SERVICE_ERROR)) {
                logger.warn(e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            logger.error(UNEXPECTED_EXCEPTION, e.getMessage());
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> create(@RequestBody TypeDTO typeDto) {
        logger.info("Process request : Create Type");
        try {
            typeService.createType(typeDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (TypeRuntimeException e) {
            if (e.getMessage().equals(TypeRuntimeException.SERVICE_ERROR)) {
                logger.warn(e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            logger.error(UNEXPECTED_EXCEPTION, e.getMessage());
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }        
    }
    
}
