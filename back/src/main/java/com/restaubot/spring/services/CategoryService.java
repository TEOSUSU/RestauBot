package com.restaubot.spring.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restaubot.spring.models.dto.CategoryDTO;
import com.restaubot.spring.models.entities.CategoryEntity;
import com.restaubot.spring.repositories.CategoryRepository;
import com.restaubot.spring.security.CategoryRuntimeException;

@Service
@Transactional
public class CategoryService {
    private static final Logger logger = LogManager.getLogger(CategoryService.class);

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<CategoryDTO> listAllCategories() throws CategoryRuntimeException {
        try {
            return categoryRepository.findAll().stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error listing all categories:", e);
            throw new CategoryRuntimeException(CategoryRuntimeException.SERVICE_ERROR);
        }
    }

    public CategoryDTO saveCategory(CategoryDTO category) throws CategoryRuntimeException {
        CategoryEntity categoryEntity = modelMapper.map(category, CategoryEntity.class);

        CategoryEntity response = null;
        try {
            response = categoryRepository.save(categoryEntity);
        } catch (Exception e) {
            logger.error("Error saving Category:", e);
            throw new CategoryRuntimeException(CategoryRuntimeException.SERVICE_ERROR);
        }

        return modelMapper.map(response, CategoryDTO.class);
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) throws CategoryRuntimeException {
        CategoryDTO category = new CategoryDTO(categoryDTO.getName());
        saveCategory(category);
        return category;
    }

    
}
