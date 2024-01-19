package com.restaubot.spring.services;

import java.io.File;
import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

import com.restaubot.spring.models.dto.RestaurantDTO;
import com.restaubot.spring.models.entities.RestaurantEntity;
import com.restaubot.spring.models.entities.SlotEntity;
import com.restaubot.spring.repositories.RestaurantRepository;
import com.restaubot.spring.repositories.SlotRepository;
import com.restaubot.spring.security.CustomRuntimeException;
import com.restaubot.spring.security.DishRuntimeException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.param.AccountCreateParams;

@Service
@Transactional
public class RestaurantService {

    private static final Logger logger = LogManager.getLogger(RestaurantService.class);
    private final String FOLDER_PATH=new File("front/src/images/restaurants").getAbsolutePath()+"\\";

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

    public RestaurantDTO createRestaurant(RestaurantDTO restaurantDTO, MultipartFile file) 
    throws CustomRuntimeException, DishRuntimeException, IllegalStateException, IOException, StripeException{
        String password = restaurantDTO.getPassword();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String bCrypPassword = bCryptPasswordEncoder.encode(password);
        RestaurantDTO restaurant = restaurantDTO;
        restaurant.setPassword(bCrypPassword);
        restaurant.setRole("ROLE_RESTAURANT");
        Optional<RestaurantEntity> optionalRestaurant = Optional.empty();
        try {
            optionalRestaurant = restaurantRepository.findByMail(restaurant.getMail());
        } catch (Exception e) {
            logger.error("Error findByLogin", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }
        if (optionalRestaurant.isEmpty()) {
            RestaurantDTO response = saveRestaurant(restaurant,file);
            Stripe.apiKey = "sk_test_51J2FJ1IpZvmzyvdyKnZHKbUoCUxmBYoO0e7SyP0I460xJGwKYajMQZtum8zIFPGWQRFHUuwNyp9UfAHmFdNG1LHj00SUSKjtnE";

            AccountCreateParams params =
                AccountCreateParams.builder().setType(AccountCreateParams.Type.STANDARD).build();

            Account account = Account.create(params);

            return modelMapper.map(response, RestaurantDTO.class);
        } else {
            throw new CustomRuntimeException(CustomRuntimeException.MAIL_TAKEN);
        }

    }

    public RestaurantDTO getRestaurantByMail(String mail) throws CustomRuntimeException {
        Optional<RestaurantEntity> optionalRestaurant = Optional.empty();
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


    public RestaurantDTO saveRestaurant(RestaurantDTO restaurant, MultipartFile file) throws CustomRuntimeException, IllegalStateException, IOException {
        RestaurantEntity restaurantEntity = modelMapper.map(restaurant, RestaurantEntity.class);

        RestaurantEntity response;
        try {
            response = restaurantRepository.save(restaurantEntity);

            String filePath = FOLDER_PATH + response.getIdUser();
            String fileName = response.getIdUser() + "." + getFileExtension(file.getOriginalFilename());

            // Vérifier l'extension du fichier
            String fileExtension = getFileExtension(file.getOriginalFilename());
            if (!isValidImageExtension(fileExtension)) {
                logger.error("Invalid file format. Only JPEG, PNG, and GIF are allowed.");
                throw new DishRuntimeException(DishRuntimeException.INVALID_FILE_FORMAT);
            }
    
            filePath += "." + fileExtension; // Ajouter l'extension au chemin du fichier
            response.setPicture("../src/images/restaurants/" + fileName);
            file.transferTo(new File(filePath));
            return modelMapper.map(response,RestaurantDTO.class);
        } catch (Exception e) {
            logger.error("Error saving restaurant:", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }
    }


    public RestaurantDTO updateRestaurant(RestaurantDTO restaurantDTO, MultipartFile file) throws CustomRuntimeException, IllegalStateException, IOException {

            RestaurantDTO response = saveRestaurant(restaurantDTO,file);
            return modelMapper.map(response, RestaurantDTO.class);
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

    

}
