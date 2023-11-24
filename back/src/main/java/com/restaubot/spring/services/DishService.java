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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import com.restaubot.spring.models.dto.DishDTO;
import com.restaubot.spring.models.entities.DishEntity;
import com.restaubot.spring.repositories.DishRepository;
import com.restaubot.spring.security.DishRuntimeException;
import com.restaubot.spring.security.CustomRuntimeException;

@Service
@Transactional
public class DishService {
    private static final Logger logger = LogManager.getLogger(DishService.class);

    private final String FOLDER_PATH=new File("front/src/images").getAbsolutePath()+"\\";

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

    public DishDTO saveDish(DishDTO dish, MultipartFile file) throws DishRuntimeException, IllegalStateException, IOException {
        DishEntity dishEntity = modelMapper.map(dish, DishEntity.class);
        
        /*if (dishEntity.getIdDish() != null){
            logger.error("Dish id should be null");
            throw new CustomRuntimeException(CustomRuntimeException.ID_CUSTOMER_SHOULD_BE_NULL);
        }*/

        DishEntity response;
        try {
            response = dishRepository.save(dishEntity);

            String filePath = FOLDER_PATH + response.getIdDish();
            String fileName = response.getIdDish() + "." + getFileExtension(file.getOriginalFilename());

            // Vérifier l'extension du fichier
            String fileExtension = getFileExtension(file.getOriginalFilename());
            if (!isValidImageExtension(fileExtension)) {
                logger.error("Invalid file format. Only JPEG, PNG, and GIF are allowed.");
                throw new DishRuntimeException(DishRuntimeException.INVALID_FILE_FORMAT);
            }
    
            filePath += "." + fileExtension; // Ajouter l'extension au chemin du fichier
            response.setPicture("../src/images/dishes/" + fileName);
            file.transferTo(new File(filePath));
        } catch (Exception e) {
            logger.error("Error saving Dish:", e);
            throw new DishRuntimeException(DishRuntimeException.SERVICE_ERROR);
        }
    
        return modelMapper.map(response, DishDTO.class);
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return ""; 
        }
        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }
    
    private boolean isValidImageExtension(String extension) {
        return extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") || extension.equals("gif");
    }

    public DishDTO createDish(DishDTO dishDTO, MultipartFile file) throws 
        IOException, DishRuntimeException {
    
        DishDTO dish = new DishDTO(dishDTO.getName(), dishDTO.getDescription(), 
        dishDTO.getPrice(), null, dishDTO.getType(), dishDTO.getRestaurant());
        saveDish(dish, file);
        return dish;
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
