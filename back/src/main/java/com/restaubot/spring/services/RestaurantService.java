package com.restaubot.spring.services;

import java.util.Optional;

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

@Service
@Transactional
public class RestaurantService {

    private static final Logger logger = LogManager.getLogger(RestaurantService.class);

    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private ModelMapper modelMapper;

    public RestaurantDTO getRestaurantById(Integer id) throws RestaurantRuntimeException {
        Optional<RestaurantEntity> optionalRestaurant = Optional.empty();
        try {
            optionalRestaurant = restaurantRepository.findById(id);
        } catch (Exception e) {
            logger.error("Error findById", e);
            throw new RestaurantRuntimeException(RestaurantRuntimeException.SERVICE_ERROR);
        }
        if (optionalRestaurant.isEmpty()) {
            throw new RestaurantRuntimeException(RestaurantRuntimeException.RESTAURANT_NOT_FOUND);
        }
        return modelMapper.map(optionalRestaurant.get(), RestaurantDTO.class);
    }
}
