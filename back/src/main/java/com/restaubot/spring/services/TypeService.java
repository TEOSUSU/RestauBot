package com.restaubot.spring.services;

import java.util.List;
import java.util.Optional;
import java.util.Locale.Category;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restaubot.spring.models.dto.CategoryDTO;
import com.restaubot.spring.models.dto.CustomerDTO;
import com.restaubot.spring.models.dto.TypeDTO;
import com.restaubot.spring.models.entities.CategoryEntity;
import com.restaubot.spring.models.entities.CustomerEntity;
import com.restaubot.spring.models.entities.TypeEntity;
import com.restaubot.spring.repositories.TypeRepository;
import com.restaubot.spring.security.CategoryRuntimeException;
import com.restaubot.spring.security.CustomRuntimeException;
import com.restaubot.spring.security.TypeRuntimeException;

@Service
@Transactional
public class TypeService {
    private static final Logger logger = LogManager.getLogger(TypeService.class);

    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<TypeDTO> listAllTypes() throws TypeRuntimeException {
        try {
            return typeRepository.findAll().stream()
                .map(type -> modelMapper.map(type, TypeDTO.class))
                .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error listing all Types:", e);
            throw new TypeRuntimeException(TypeRuntimeException.SERVICE_ERROR);
        }
    }
    
    public TypeDTO saveType(TypeDTO type) throws TypeRuntimeException {
        TypeEntity typeEntity = modelMapper.map(type, TypeEntity.class);

        TypeEntity response = null;
        try {
            response = typeRepository.save(typeEntity);
        } catch (Exception e) {
            logger.error("Error saving Type:", e);
            throw new TypeRuntimeException(TypeRuntimeException.SERVICE_ERROR);
        }

        return modelMapper.map(response, TypeDTO.class);
    }

    public TypeDTO createType(TypeDTO typeDTO) throws TypeRuntimeException {
        TypeDTO type = new TypeDTO(typeDTO.getName(), typeDTO.getCategory());
        saveType(type);
        return type;
    }
}
