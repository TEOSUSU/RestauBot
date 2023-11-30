package com.restaubot.spring.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.restaubot.spring.models.dto.DishDTO;
import com.restaubot.spring.models.dto.MenuDTO;
import com.restaubot.spring.models.entities.CategoryEntity;
import com.restaubot.spring.models.entities.DishEntity;
import com.restaubot.spring.models.entities.MenuEntity;
import com.restaubot.spring.models.entities.RestaurantEntity;
import com.restaubot.spring.repositories.DishRepository;
import com.restaubot.spring.repositories.MenuRepository;
import com.restaubot.spring.security.CustomRuntimeException;
import com.restaubot.spring.security.MenuRunTimeException;

@Service
@Transactional
public class MenuService {
    private static final Logger logger = LogManager.getLogger(MenuService.class);

    private final String FOLDER_PATH=new File("images").getAbsolutePath()+"\\";

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private DishRepository dishRepository;
    @Autowired
    private ModelMapper modelMapper;
    
    public MenuDTO saveMenu(MenuDTO menu, MultipartFile file, 
    List<Integer> dishesId) throws MenuRunTimeException {
        MenuEntity menuEntity = modelMapper.map(menu, MenuEntity.class);

        MenuEntity response = null;
        try {
            response = menuRepository.save(menuEntity);
            String filePath=FOLDER_PATH+response.getIdMenu();
            response.setPicture(filePath);
            file.transferTo(new File(filePath));
            Set<DishEntity> dishSet = null;
            for (Integer dishId : dishesId) {
                DishEntity dishEntity = dishRepository.findById(dishId).get();
                dishSet =  menuEntity.getAssignedDishes();
                dishSet.add(dishEntity);
            }
            menuEntity.setAssignedDishes(dishSet);
        } catch (Exception e) {
            logger.error("Error saving menu:", e);
            throw new MenuRunTimeException(MenuRunTimeException.SERVICE_ERROR);
        }

        return modelMapper.map(response, MenuDTO.class);
    }

    public MenuDTO createMenu(MenuDTO menuDTO, MultipartFile file, 
    List<Integer> dishesId) throws MenuRunTimeException {
        MenuDTO menu = new MenuDTO(menuDTO.getName(), menuDTO.getDescription(),
        menuDTO.getPrice(), null, menuDTO.getRestaurant());
        saveMenu(menu, file, dishesId);
        return menu;
    }

    public List<MenuDTO> getMenuDetails(Integer purchaseId) throws CustomRuntimeException {
        try { 
            List<MenuEntity> menuDetails = new ArrayList<>();
            menuDetails.addAll(menuRepository.findMenuDetailsByPurchaseId(purchaseId));
            return menuDetails.stream()
                    .map(purchase -> modelMapper.map(purchase, MenuDTO.class))
                    .collect(Collectors.toList());
            // return menuDetails;
        } catch (Exception e){
            logger.error("Error getting menu details:", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }
    }
}
