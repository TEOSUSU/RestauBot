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

import com.restaubot.spring.models.dto.RestaurantDTO;
import com.restaubot.spring.models.entities.RestaurantEntity;
import com.restaubot.spring.repositories.RestaurantRepository;
import com.restaubot.spring.security.RestaurantRuntimeException;
import com.restaubot.spring.security.CustomRuntimeException;

@Service
@Transactional
public class RestaurantService {

    private static final Logger logger = LogManager.getLogger(RestaurantService.class);

    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<RestaurantDTO> listAllRestaurants() throws CustomRuntimeException {
        try {
            return restaurantRepository.findAll().stream()
                .map(restaurant -> modelMapper.map(restaurant, RestaurantDTO.class))
                .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error listing all restaurants:", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }
    }

     public RestaurantDTO createRestaurant(RestaurantDTO restaurant) throws CustomRuntimeException {
        RestaurantEntity restaurantEntity = modelMapper.map(restaurant, RestaurantEntity.class);
        
        RestaurantEntity response = null;
        try {
            response = restaurantRepository.save(restaurantEntity);
        } catch (Exception e) {
            logger.error("Error creating restaurant:", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }

        return modelMapper.map(response, RestaurantDTO.class);
    }

    public RestaurantDTO getRestaurantByMail(String mail) throws CustomRuntimeException {
        Optional<RestaurantEntity> optionalRestaurant = Optional.empty();
        System.out.println(mail);
        try{
            optionalRestaurant = restaurantRepository.findByMail(mail);
        } catch (Exception e) {
            logger.error("Error findByLogin", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }
        if (optionalRestaurant.isEmpty()) {
            throw new CustomRuntimeException(CustomRuntimeException.CUSTOMER_NOT_FOUND);
        }
        return modelMapper.map(optionalRestaurant.get(), RestaurantDTO.class);
    }

    public RestaurantDTO getRestaurantById(Integer id) throws CustomRuntimeException {
        Optional<RestaurantEntity> optionalRestaurant = Optional.empty();
        try{
            optionalRestaurant = restaurantRepository.findById(id);
        } catch (Exception e) {
            logger.error("Error findByLogin", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }
        if (optionalRestaurant.isEmpty()) {
            throw new CustomRuntimeException(CustomRuntimeException.RESTAURANT_NOT_FOUND);
        }
        return modelMapper.map(optionalRestaurant.get(), RestaurantDTO.class);
    }
}
