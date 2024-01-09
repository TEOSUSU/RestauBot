package com.restaubot.spring.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restaubot.spring.models.dto.PurchaseDTO;
import com.restaubot.spring.models.entities.CustomerEntity;
import com.restaubot.spring.models.entities.DishEntity;
import com.restaubot.spring.models.entities.MenuEntity;
import com.restaubot.spring.models.entities.PurchaseEntity;
import com.restaubot.spring.models.entities.RestaurantEntity;
import com.restaubot.spring.repositories.CustomerRepository;
import com.restaubot.spring.repositories.DishRepository;
import com.restaubot.spring.repositories.MenuRepository;
import com.restaubot.spring.repositories.PurchaseRepository;
import com.restaubot.spring.repositories.RestaurantRepository;
import com.restaubot.spring.security.CustomRuntimeException;

@Service
@Transactional
public class PurchaseService {
    private static final Logger logger = LogManager.getLogger(PurchaseService.class);

    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private DishRepository dishRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<PurchaseDTO> listAllPurchases() throws CustomRuntimeException {
        try {
            return purchaseRepository.findAll().stream()
                .map(purchase -> modelMapper.map(purchase, PurchaseDTO.class))
                .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error listing all purchases:", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }
    }

    public List<PurchaseDTO> getPurchasesByCustomerAndRestaurant(Integer customerId, Integer restaurantId) throws CustomRuntimeException {
        try {
            List<PurchaseEntity> purchases = purchaseRepository.getPurchasesByCustomerIdAndRestaurantId(customerId, restaurantId);
            return purchases.stream()
                    .map(purchase -> modelMapper.map(purchase, PurchaseDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error retrieving purchases:", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }
    }

    public PurchaseDTO createPurchase(PurchaseDTO purchaseDTO) throws CustomRuntimeException {
        PurchaseEntity purchaseEntity = modelMapper.map(purchaseDTO, PurchaseEntity.class);

        logger.info(purchaseDTO.toString());

        Integer customerId = 1;

        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomRuntimeException(CustomRuntimeException.CUSTOMER_NOT_FOUND));

        purchaseEntity.setCustomer(customer);

        Integer restaurantId = purchaseDTO.getRestaurant().getIdUser();

        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new CustomRuntimeException(CustomRuntimeException.CUSTOMER_NOT_FOUND));

        purchaseEntity.setRestaurant(restaurant);

        List<Integer> dishIds = purchaseDTO.getAssignedDish().stream()
            .map(dishDTO -> dishDTO.getIdDish())
            .collect(Collectors.toList());

        logger.info(dishIds);

        List<DishEntity> dishes = new ArrayList<>();

        for (Integer dishId : dishIds) {
            Optional<DishEntity> optionalDish = dishRepository.findById(dishId);
            optionalDish.ifPresent(dishes::add);
        }

        for (int i = 0; i < dishes.size(); i++) {
            logger.info(dishes.get(i));
        }

        purchaseEntity.setAssignedDish(dishes);

        List<Integer> menuIds = purchaseDTO.getAssignedMenu().stream()
            .map(menuDTO -> menuDTO.getIdMenu())
            .collect(Collectors.toList());

        logger.info(menuIds);

        List<MenuEntity> menus = new ArrayList<>();

        for (Integer menuId : menuIds) {
            Optional<MenuEntity> optionalMenu = menuRepository.findById(menuId);
            optionalMenu.ifPresent(menus::add);
        }

        for (int i = 0; i < menus.size(); i++) {
            logger.info(menus.get(i));
        }

        purchaseEntity.setAssignedMenu(menus);

        PurchaseEntity response = null;
        try {
            response = purchaseRepository.save(purchaseEntity);
        } catch (Exception e) {
            logger.error("Error creating purchase:", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }

        return modelMapper.map(response, PurchaseDTO.class);
    }

    public List<PurchaseDTO> getPurchasesByCustomer(Integer customerId) throws CustomRuntimeException {
        try {
            List<PurchaseEntity> purchases = purchaseRepository.getPurchasesByCustomerId(customerId);
            return purchases.stream()
                    .map(purchase -> modelMapper.map(purchase, PurchaseDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error retrieving purchases:", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }
    }

    public PurchaseDTO getPurchaseById(Integer purchaseId) throws CustomRuntimeException {
        Optional<PurchaseEntity> optionalPurchase = Optional.empty();
        try {
            optionalPurchase = purchaseRepository.findById(purchaseId);
        } catch (Exception e) {
            logger.error("Error findById", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }
        if (optionalPurchase.isEmpty()) {
            throw new CustomRuntimeException(CustomRuntimeException.CUSTOMER_NOT_FOUND);
        }
        return modelMapper.map(optionalPurchase.get(), PurchaseDTO.class);
    }

    public List<PurchaseDTO> getPurchasesByRestaurant(Integer restaurantId) throws CustomRuntimeException {
        try {
            RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                    .orElseThrow(() -> new CustomRuntimeException(CustomRuntimeException.CUSTOMER_NOT_FOUND));

            List<PurchaseEntity> purchases = purchaseRepository.getPurchasesByRestaurant(restaurant);
            return purchases.stream()
                    .map(purchase -> modelMapper.map(purchase, PurchaseDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error retrieving purchases by restaurant:", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }
    }

    public PurchaseDTO updatePurchasesCollected(PurchaseDTO purchaseDTO) throws CustomRuntimeException {
        try {
            Integer purchaseId = purchaseDTO.getIdPurchase();
            PurchaseEntity existingPurchase = purchaseRepository.findById(purchaseId)
                    .orElseThrow(() -> new CustomRuntimeException(CustomRuntimeException.PURCHASE_NOT_FOUND));

            existingPurchase.setCollected(!purchaseDTO.isCollected());

            PurchaseEntity updatedPurchase = purchaseRepository.save(existingPurchase);

            return modelMapper.map(updatedPurchase, PurchaseDTO.class);
        } catch (Exception e) {
            logger.error("Error updating purchase collected status:", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }
    }
}
