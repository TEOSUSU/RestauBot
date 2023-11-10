package com.restaubot.spring.services;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restaubot.spring.models.dto.MenuDTO;
import com.restaubot.spring.models.entities.MenuEntity;
import com.restaubot.spring.repositories.MenuRepository;
import com.restaubot.spring.security.MenuRunTimeException;

@Service
@Transactional
public class MenuService {
    private static final Logger logger = LogManager.getLogger(MenuService.class);

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private ModelMapper modelMapper;
    
    public MenuDTO saveMenu(MenuDTO menu) throws MenuRunTimeException {
        MenuEntity menuEntity = modelMapper.map(menu, MenuEntity.class);

        MenuEntity response = null;
        try {
            response = menuRepository.save(menuEntity);
        } catch (Exception e) {
            logger.error("Error saving menu:", e);
            throw new MenuRunTimeException(MenuRunTimeException.SERVICE_ERROR);
        }

        return modelMapper.map(response, MenuDTO.class);
    }

    public MenuDTO createMenu(MenuDTO menuDTO) throws MenuRunTimeException {
        MenuDTO menu = new MenuDTO(menuDTO.getName(), menuDTO.getDescription(),
        menuDTO.getPrice(), menuDTO.getPicture(), menuDTO.getRestaurant());
        saveMenu(menu);
        return menu;
    }
}
