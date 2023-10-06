package com.restaubot.spring.controllers;

import com.restaubot.spring.security.CustomRuntimeException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo")
public class DemonstrationController {

    private static final Logger logger = LogManager.getLogger(DemonstrationController.class);
    private static final String UNEXPECTED_EXCEPTION = "Unexpected Exception : {}";

    //localhost:8080/demo/helloworld/<Inserer un nom> 
    //GET
    @GetMapping("/helloworld/{name}")
    public ResponseEntity<String> getInformation(@PathVariable String name) throws CustomRuntimeException {
        logger.info("Process request : get information");
        return new ResponseEntity<>("Bonjour " + name, HttpStatus.OK);
    }
}