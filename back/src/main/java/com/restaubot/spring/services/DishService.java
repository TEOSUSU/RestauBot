package com.restaubot.spring.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restaubot.spring.models.dto.DishDTO;
import com.restaubot.spring.models.entities.DishEntity;
import com.restaubot.spring.repositories.DishRepository;
import com.restaubot.spring.security.CustomRuntimeException;

@Service
@Transactional
public class DishService {

    private static final Logger logger = LogManager.getLogger(DishService.class);

    @Autowired
    private DishRepository dishRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<DishDTO> listAllDishes() throws CustomRuntimeException {
        try {
            return dishRepository.findAll().stream()
                .map(dish -> modelMapper.map(dish, DishDTO.class))
                .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error listing all dishes:", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }
    }

    public DishDTO getDishById(Integer id) throws CustomRuntimeException {
        Optional<DishEntity> optionalDish = Optional.empty();
        try {
            optionalDish = dishRepository.findById(id);
        } catch (Exception e) {
            logger.error("Error findById", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }
        if (optionalDish.isEmpty()) {
            throw new CustomRuntimeException(CustomRuntimeException.CUSTOMER_NOT_FOUND);
        }
        return modelMapper.map(optionalDish.get(), DishDTO.class);
    }

    public DishDTO saveDish(DishDTO dish) throws CustomRuntimeException {
        DishEntity dishEntity = modelMapper.map(dish, DishEntity.class);
        
        /*if (dishEntity.getIdDish() != null){
            logger.error("Dish id should be null");
            throw new CustomRuntimeException(CustomRuntimeException.ID_CUSTOMER_SHOULD_BE_NULL);
        }*/

        DishEntity response = null;
        try {
            response = dishRepository.save(dishEntity);
        } catch (Exception e) {
            logger.error("Error saving dish:", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }

        return modelMapper.map(response, DishDTO.class);
    }

    public DishDTO updateDish(DishDTO dish) throws CustomRuntimeException {
        DishEntity dishEntity = modelMapper.map(dish, DishEntity.class);
        
        Optional<DishEntity> optionalDish = dishRepository.findById(dishEntity.getIdDish());
        if (optionalDish.isEmpty()){
            throw new CustomRuntimeException(CustomRuntimeException.CUSTOMER_NOT_FOUND);
        }

        DishEntity response = null;
        try {
            response = dishRepository.save(dishEntity);
        } catch (Exception e) {
            logger.error("Error updating dish:", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }

        return modelMapper.map(response, DishDTO.class);
    }

    public void deleteDishById(Integer id) throws CustomRuntimeException {
        dishRepository.deleteById(id);
    }

}
