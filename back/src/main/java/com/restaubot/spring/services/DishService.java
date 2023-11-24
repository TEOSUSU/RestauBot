package com.restaubot.spring.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import com.restaubot.spring.models.dto.DishDTO;
import com.restaubot.spring.models.dto.MenuDTO;
import com.restaubot.spring.models.entities.DishEntity;
import com.restaubot.spring.models.entities.MenuEntity;
import com.restaubot.spring.models.entities.TypeEntity;
import com.restaubot.spring.repositories.DishRepository;
import com.restaubot.spring.repositories.TypeRepository;
import com.restaubot.spring.security.DishRuntimeException;
import com.restaubot.spring.security.MenuRunTimeException;
import com.restaubot.spring.security.CustomRuntimeException;

@Service
@Transactional
public class DishService {
    private static final Logger logger = LogManager.getLogger(DishService.class);

    private final String FOLDER_PATH=new File("images").getAbsolutePath()+"\\";

    @Autowired
    private DishRepository dishRepository;
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<DishDTO> listAllDishes() throws CustomRuntimeException {
        try {
            return dishRepository.findByDeletedFalse().stream()
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

    public DishDTO saveDish(DishDTO dish, MultipartFile file) throws DishRuntimeException, IllegalStateException, IOException {
        DishEntity dishEntity = modelMapper.map(dish, DishEntity.class);
        
        /*if (dishEntity.getIdDish() != null){
            logger.error("Dish id should be null");
            throw new CustomRuntimeException(CustomRuntimeException.ID_CUSTOMER_SHOULD_BE_NULL);
        }*/

        DishEntity response = null;
        try {
            response = dishRepository.save(dishEntity);
            String filePath=FOLDER_PATH+response.getIdDish();
            response.setPicture(filePath);
            response.setAvailable(true);
            response.setDeleted(false);
            file.transferTo(new File(filePath));
        } catch (Exception e) {
            logger.error("Error saving Dish:", e);
            throw new DishRuntimeException(DishRuntimeException.SERVICE_ERROR);
        }

        return modelMapper.map(response, DishDTO.class);
    }

    public DishDTO createDish(DishDTO dishDTO, MultipartFile file) throws 
        IOException, DishRuntimeException {
    
        DishDTO dish = new DishDTO(dishDTO.getName(), dishDTO.getDescription(), 
        dishDTO.getPrice(), null, dishDTO.getType(), dishDTO.getRestaurant());
        saveDish(dish, file);
        return dish;
    }

    public DishDTO modifyDish(DishDTO dishDTO, MultipartFile file, 
    Integer dishId, Integer typeId) throws CustomRuntimeException {
        DishEntity dish = null;
        try {
            dish = dishRepository.getReferenceById(dishId);
            dish.setName(dishDTO.getName());
            dish.setDescription(dishDTO.getDescription());
            dish.setPrice(dishDTO.getPrice());
            TypeEntity type = typeRepository.getReferenceById(typeId);
            dish.setType(type);
            String filePath=FOLDER_PATH+dish.getIdDish();
            dish.setPicture(filePath);
            file.transferTo(new File(filePath));
        } catch (Exception e) {
            logger.error("Error findById", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }
        return modelMapper.map(dish, DishDTO.class);
    }

    public ResponseEntity<String> deleteDish(Integer id) throws CustomRuntimeException {
        DishEntity dish = null;
        try {
            dish = dishRepository.getReferenceById(id);
            dish.setAvailable(false);
            dish.setDeleted(true);
        } catch (Exception e) {
            logger.error("Error findById", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }
        return ResponseEntity.ok().body(dish.getIdDish() + "  supprimée avec succès.");
    }
}
