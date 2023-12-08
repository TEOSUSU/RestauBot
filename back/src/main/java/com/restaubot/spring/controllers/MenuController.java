package com.restaubot.spring.controllers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.restaubot.spring.models.dto.DishDTO;
import com.restaubot.spring.models.dto.MenuDTO;
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

    
    @GetMapping("/details/{purchaseId}")
    public ResponseEntity<List<MenuDTO>> getMenuDetails(@PathVariable Integer purchaseId) {
        logger.info("Process request : Get menu details by purchase id : {}", purchaseId);
        try {
            List<MenuDTO> menues = menuService.getMenuDetails(purchaseId);
            return new ResponseEntity<>(menues, HttpStatus.OK);
            } catch (CustomRuntimeException e) {
            if (e.getMessage().equals(CustomRuntimeException.SERVICE_ERROR)) {
                logger.warn(e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            logger.error(UNEXPECTED_EXCEPTION, e.getMessage());
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }        
    }

    @PostMapping("/modify/{menuId}")
    public ResponseEntity<HttpStatus> modify(@ModelAttribute MenuDTO menuDto,
    @RequestParam("file") MultipartFile file,
    @RequestParam("dishes") List<Integer> dishesId,
    @PathVariable Integer menuId) {
        logger.info("Process request : Modify menu");
        try {
            menuService.modifyMenu(menuDto, file, dishesId, menuId);
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

    @PostMapping("/delete/{menuId}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Integer menuId) {
        logger.info("Process request : Delete menu");
        try {
            menuService.deleteMenu(menuId);
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

    @GetMapping("/{idMenu}")
    public ResponseEntity<MenuDTO> getMenuById(@PathVariable Integer idMenu) {
        logger.info("Process request : List menu");
        try {
            MenuDTO menu = menuService.getMenuById(idMenu);
            return new ResponseEntity<>(menu, HttpStatus.OK);
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
    public ResponseEntity<List<MenuDTO>> list() {
        logger.info("Process request : List all menus");
        try {
            List<MenuDTO> menus = menuService.listAllMenus();
            return new ResponseEntity<>(menus, HttpStatus.OK);
        } catch (CustomRuntimeException e) {
            if (e.getMessage().equals(CustomRuntimeException.SERVICE_ERROR)) {
                logger.warn(e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            logger.error(UNEXPECTED_EXCEPTION, e.getMessage());
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }        
    }

    @PostMapping("/toggleAvailability/{id_menu}")
    public ResponseEntity<MenuDTO> toggleAvailability(@PathVariable Integer id_menu) {
        logger.info("Process request : Toggle menu availability");
        try {
            MenuDTO menu = menuService.toggleMenuAvailability(id_menu);
            return new ResponseEntity<>(menu, HttpStatus.OK);
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
