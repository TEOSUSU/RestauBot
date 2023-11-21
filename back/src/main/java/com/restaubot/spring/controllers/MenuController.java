package com.restaubot.spring.controllers;

import java.util.List;

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

import com.restaubot.spring.models.dto.MenuDTO;
import com.restaubot.spring.security.CustomRuntimeException;
import com.restaubot.spring.security.MenuRunTimeException;
import com.restaubot.spring.services.MenuService;
import com.restaubot.spring.services.RestaurantService;

@RestController
@RequestMapping("/api/menus")
public class MenuController {
    
    private static final Logger logger = LogManager.getLogger(MenuController.class);
    private static final String UNEXPECTED_EXCEPTION = "Unexpected Exception : {}";

    @Autowired
    MenuService menuService;
    @Autowired
    RestaurantService restaurantService;

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> create(@ModelAttribute MenuDTO menuDto,
    @RequestParam("restaurantId") Integer restaurantId,
    @RequestParam("file") MultipartFile file,
    @RequestParam("dishes") List<Integer> dishesId) throws CustomRuntimeException {
        logger.info("Process request : Create menu");
        try {
            menuDto.setRestaurant(restaurantService.getRestaurantById(restaurantId));
            menuService.createMenu(menuDto, file, dishesId);
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
