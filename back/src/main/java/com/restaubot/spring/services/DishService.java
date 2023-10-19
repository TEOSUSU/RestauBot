package com.restaubot.spring.services;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restaubot.spring.models.dto.DishDTO;
import com.restaubot.spring.models.entities.DishEntity;
import com.restaubot.spring.repositories.DishRepository;
import com.restaubot.spring.security.DishRuntimeException;

@Service
@Transactional
public class DishService {
    private static final Logger logger = LogManager.getLogger(DishService.class);

    @Autowired
    private DishRepository dishRepository;
    @Autowired
    private ModelMapper modelMapper;

    public DishDTO saveDish(DishDTO dish) throws DishRuntimeException {
        DishEntity dishEntity = modelMapper.map(dish, DishEntity.class);
        
        /*if (DishEntity.getIdDish() != null){
            logger.error("Dish id should be null");
            throw new DishRuntimeException(DishRuntimeException.ID_Dish_SHOULD_BE_NULL);
        }*/

        DishEntity response = null;
        try {
            response = dishRepository.save(dishEntity);
        } catch (Exception e) {
            logger.error("Error saving Dish:", e);
            throw new DishRuntimeException(DishRuntimeException.SERVICE_ERROR);
        }

        return modelMapper.map(response, DishDTO.class);
    }

    public DishDTO createDish(DishDTO dishDTO) throws DishRuntimeException {
        DishDTO dish = new DishDTO(dishDTO.getName(), dishDTO.getDescription(), 
        dishDTO.getPrice(), dishDTO.getPicture(), dishDTO.getType(), dishDTO.getRestaurant());
        saveDish(dish);
        return dish;
    }
}
