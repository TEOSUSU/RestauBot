package com.restaubot.spring.services;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import com.restaubot.spring.models.dto.DishDTO;
import com.restaubot.spring.models.entities.DishEntity;
import com.restaubot.spring.repositories.DishRepository;
import com.restaubot.spring.security.DishRuntimeException;

@Service
@Transactional
public class DishService {
    private static final Logger logger = LogManager.getLogger(DishService.class);

    private final String FOLDER_PATH=new File("images").getAbsolutePath()+"\\";

    private static long dishIdCounter = 0; // Variable pour stocker le compteur d'ID

    @Autowired
    private DishRepository dishRepository;
    @Autowired
    private ModelMapper modelMapper;

    public DishDTO saveDish(DishDTO dish, MultipartFile file) throws DishRuntimeException, IllegalStateException, IOException {
        DishEntity dishEntity = modelMapper.map(dish, DishEntity.class);

        DishEntity response = null;
        try {
            response = dishRepository.save(dishEntity);
        } catch (Exception e) {
            logger.error("Error saving Dish:", e);
            throw new DishRuntimeException(DishRuntimeException.SERVICE_ERROR);
        }

        String filePath=FOLDER_PATH+response.getIdDish();
        response.setPicture(filePath);
        file.transferTo(new File(filePath));

        return modelMapper.map(response, DishDTO.class);
    }

    public DishDTO createDish(DishDTO dishDTO, MultipartFile file) throws 
        IOException, DishRuntimeException {
    
        DishDTO dish = new DishDTO(dishDTO.getName(), dishDTO.getDescription(), 
        dishDTO.getPrice(), null, dishDTO.getType(), dishDTO.getRestaurant());
        saveDish(dish, file);
        return dish;
    }
}
