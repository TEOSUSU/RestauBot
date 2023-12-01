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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.restaubot.spring.models.dto.PersonDTO;
import com.restaubot.spring.models.dto.RestaurantDTO;
import com.restaubot.spring.models.entities.RestaurantEntity;
import com.restaubot.spring.models.entities.SlotEntity;
import com.restaubot.spring.repositories.RestaurantRepository;
import com.restaubot.spring.repositories.SlotRepository;
import com.restaubot.spring.security.CustomRuntimeException;

@Service
@Transactional
public class RestaurantService {

    private static final Logger logger = LogManager.getLogger(RestaurantService.class);

    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    SlotRepository slotRepository;

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

    public RestaurantDTO createRestaurant(RestaurantDTO restaurantDTO) throws CustomRuntimeException {
        PersonDTO personDTO = 
        String password = restaurantDTO.getPassword();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String bCrypPassword = bCryptPasswordEncoder.encode(password);
        RestaurantDTO restaurant = restaurantDTO;
        restaurant.setPassword(bCrypPassword);
        Optional<RestaurantEntity> optionalRestaurant = Optional.empty();
        try {
            optionalRestaurant = restaurantRepository.findByMail(restaurantDTO.getMail());
        } catch (Exception e) {
            logger.error("Error findByLogin", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }
        if (optionalRestaurant.isEmpty()) {
            RestaurantEntity response = saveRestaurant(restaurant);
            return modelMapper.map(response, RestaurantDTO.class);
        } else {
            throw new CustomRuntimeException(CustomRuntimeException.MAIL_TAKEN);
        }

    }

    public RestaurantDTO getRestaurantByMail(String mail) throws CustomRuntimeException {
        Optional<RestaurantEntity> optionalRestaurant = Optional.empty();
        System.out.println(mail);
        try {
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

    public RestaurantEntity assignRestaurantToSlot(Integer restaurantId, Integer slotId) {
        Set<SlotEntity> slotSet = null;
        RestaurantEntity restaurantEntity = restaurantRepository.findById(restaurantId).get();
        SlotEntity slot = slotRepository.findById(slotId).get();
        slotSet = restaurantEntity.getAssignedSlot();
        slotSet.add(slot);
        restaurantEntity.setAssignedSlot(slotSet);
        modelMapper.map(restaurantEntity, RestaurantDTO.class);
        return restaurantRepository.save(restaurantEntity);
    }

    public RestaurantDTO getRestaurantById(Integer id) throws CustomRuntimeException {
        Optional<RestaurantEntity> optionalRestaurant = Optional.empty();
        try {
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

    public RestaurantEntity saveRestaurant(RestaurantDTO restaurant) throws CustomRuntimeException {
        RestaurantEntity restaurantEntity = modelMapper.map(restaurant, RestaurantEntity.class);

        try {
            return restaurantRepository.save(restaurantEntity);
        } catch (Exception e) {
            logger.error("Error saving restaurant:", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }
    }

}
