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

import com.restaubot.spring.models.dto.MenuDTO;
import com.restaubot.spring.security.MenuRunTimeException;
import com.restaubot.spring.services.MenuService;

@RestController
@RequestMapping("/api/menus")
public class MenuController {
    
    private static final Logger logger = LogManager.getLogger(MenuController.class);
    private static final String UNEXPECTED_EXCEPTION = "Unexpected Exception : {}";

    @Autowired
    MenuService menuService;

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> create(@RequestBody MenuDTO menuDto) {
        logger.info("Process request : Create menu");
        try {
            menuService.createMenu(menuDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (MenuRunTimeException e) {
            if (e.getMessage().equals(MenuRunTimeException.SERVICE_ERROR)) {
                logger.warn(e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            logger.error(UNEXPECTED_EXCEPTION, e.getMessage());
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }        
    }
    
}
