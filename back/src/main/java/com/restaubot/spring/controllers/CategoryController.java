package com.restaubot.spring.controllers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restaubot.spring.models.dto.CategoryDTO;
import com.restaubot.spring.security.CustomRuntimeException;
import com.restaubot.spring.services.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private static final Logger logger = LogManager.getLogger(CustomerController.class);
    private static final String UNEXPECTED_EXCEPTION = "Unexpected Exception : {}";

    @Autowired
    CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<List<CategoryDTO>> list() {
        logger.info("Process request : List all categories");
        try {
            List<CategoryDTO> categories = categoryService.listAllCategories();
            return new ResponseEntity<>(categories, HttpStatus.OK);
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