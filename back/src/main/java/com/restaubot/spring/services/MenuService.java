package com.restaubot.spring.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.restaubot.spring.security.DishRuntimeException;
import com.restaubot.spring.security.MenuRunTimeException;

@Service
@Transactional
public class MenuService {
    private static final Logger logger = LogManager.getLogger(MenuService.class);

    private final String FOLDER_PATH=new File("front/src/images/menu").getAbsolutePath()+"\\";

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private DishRepository dishRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<MenuDTO> listAllMenus() throws CustomRuntimeException {
        try {
            return menuRepository.findByDeletedFalse().stream()
                .map(menu -> modelMapper.map(menu, MenuDTO.class))
                .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error listing all menus:", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }
    }
    
    public MenuDTO saveMenu(MenuDTO menu, MultipartFile file, 
    List<Integer> dishesId) throws MenuRunTimeException {
        MenuEntity menuEntity = modelMapper.map(menu, MenuEntity.class);

        MenuEntity response = null;
        try {
            response = menuRepository.save(menuEntity);
            response.setAvailable(true);
            response.setDeleted(false);
            String filePath = FOLDER_PATH + response.getIdMenu();
            String fileName = response.getIdMenu() + "." + getFileExtension(file.getOriginalFilename());

            // Vérifier l'extension du fichier
            String fileExtension = getFileExtension(file.getOriginalFilename());
            if (!isValidImageExtension(fileExtension)) {
                logger.error("Invalid file format. Only JPEG, PNG, and GIF are allowed.");
                throw new DishRuntimeException(DishRuntimeException.INVALID_FILE_FORMAT);
            }
    
            filePath += "." + fileExtension; // Ajouter l'extension au chemin du fichier
            response.setPicture("../src/images/menu/" + fileName);
            file.transferTo(new File(filePath));
            Set<DishEntity> dishSet = menuEntity.getAssignedDishes();
            if (dishSet == null) {
                    dishSet = new HashSet<>(); // or any other Set implementation you prefer
                }
            for (Integer dishId : dishesId) {
                DishEntity dishEntity = dishRepository.findById(dishId).get();
                dishSet.add(dishEntity);
            }
            menuEntity.setAssignedDishes(dishSet);
        } catch (Exception e) {
            logger.error("Error saving menu:", e);
            throw new MenuRunTimeException(MenuRunTimeException.SERVICE_ERROR);
        }

        return modelMapper.map(response, MenuDTO.class);
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

    public MenuDTO createMenu(MenuDTO menuDTO, MultipartFile file, 
    List<Integer> dishesId) throws MenuRunTimeException {
        MenuDTO menu = new MenuDTO(menuDTO.getName(), menuDTO.getDescription(),
        menuDTO.getPrice(), null, menuDTO.getRestaurant());
        saveMenu(menu, file, dishesId);
        return menu;
    }

    public MenuDTO modifyMenu(MenuDTO menuDTO, MultipartFile file, 
    List<Integer> dishesId, Integer menuId) throws MenuRunTimeException {
        MenuEntity menu = null;
        try {
            menu = menuRepository.getReferenceById(menuId);
            menu.setName(menuDTO.getName());
            menu.setDescription(menuDTO.getDescription());
            menu.setPrice(menuDTO.getPrice());
            String filePath = FOLDER_PATH + menu.getIdMenu();
            String fileName = menu.getIdMenu() + "." + getFileExtension(file.getOriginalFilename());

            // Vérifier l'extension du fichier
            String fileExtension = getFileExtension(file.getOriginalFilename());
            if (!isValidImageExtension(fileExtension)) {
                logger.error("Invalid file format. Only JPEG, PNG, and GIF are allowed.");
                throw new DishRuntimeException(DishRuntimeException.INVALID_FILE_FORMAT);
            }
    
            filePath += "." + fileExtension; // Ajouter l'extension au chemin du fichier
            menu.setPicture("../src/images/menu/" + fileName);
            file.transferTo(new File(filePath));
            Set<DishEntity> dishSet = null;
            for (Integer dishId : dishesId) {
                DishEntity dishEntity = dishRepository.findById(dishId).get();
                dishSet =  menu.getAssignedDishes();
                dishSet.add(dishEntity);
            }
            menu.setAssignedDishes(dishSet);
        } catch (Exception e) {
            logger.error("Error findById", e);
            throw new MenuRunTimeException(MenuRunTimeException.SERVICE_ERROR);
        }
        return modelMapper.map(menu, MenuDTO.class);
    }

    public ResponseEntity<String> deleteMenu(Integer menuId) throws MenuRunTimeException {
        MenuEntity menu = null;
        try {
            menu = menuRepository.getReferenceById(menuId);
            menu.setAvailable(false);
            menu.setDeleted(true);
        } catch (Exception e) {
            logger.error("Error findById", e);
            throw new MenuRunTimeException(MenuRunTimeException.SERVICE_ERROR);
        }
        return ResponseEntity.ok().body(menu.getIdMenu() + "  supprimée avec succès.");
    }

    public MenuDTO getMenuById(Integer id) throws CustomRuntimeException {
        Optional<MenuEntity> optionalMenu = Optional.empty();
        try {
            optionalMenu = menuRepository.findByIdAndDeletedFalse(id);
        } catch (Exception e) {
            logger.error("Error findById", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }
        if (optionalMenu.isEmpty()) {
            throw new CustomRuntimeException(CustomRuntimeException.CUSTOMER_NOT_FOUND);
        }
        return modelMapper.map(optionalMenu.get(), MenuDTO.class);
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

    public MenuDTO toggleMenuAvailability(Integer id_menu) throws MenuRunTimeException {
        MenuEntity menu = null;
        try {
            menu = menuRepository.getReferenceById(id_menu);
            menu.setAvailable(!menu.isAvailable());
        } catch (Exception e) {
            logger.error("Error findById", e);
            throw new MenuRunTimeException(MenuRunTimeException.SERVICE_ERROR);
        }
        return modelMapper.map(menu, MenuDTO.class);
    }
}
