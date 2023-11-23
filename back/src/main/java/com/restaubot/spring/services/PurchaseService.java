package com.restaubot.spring.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.restaubot.spring.models.dto.DishDTO;
import com.restaubot.spring.models.dto.PurchaseDTO;
import com.restaubot.spring.models.entities.CustomerEntity;
import com.restaubot.spring.models.entities.DishEntity;
import com.restaubot.spring.models.entities.PurchaseEntity;
import com.restaubot.spring.repositories.CustomerRepository;
import com.restaubot.spring.repositories.DishRepository;
import com.restaubot.spring.repositories.PurchaseRepository;
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
    private CustomerRepository customerRepository;

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

    public PurchaseDTO createPurchase(PurchaseDTO purchaseDTO) throws CustomRuntimeException {
    PurchaseEntity purchaseEntity = modelMapper.map(purchaseDTO, PurchaseEntity.class);

    logger.info(purchaseDTO.toString());;

    Integer customerId = 1;

    CustomerEntity customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomRuntimeException(CustomRuntimeException.CUSTOMER_NOT_FOUND));

    purchaseEntity.setCustomer(customer);

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

    PurchaseEntity response = null;
    try {
        response = purchaseRepository.save(purchaseEntity);
    } catch (Exception e) {
        logger.error("Error creating purchase:", e);
        throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
    }

    return modelMapper.map(response, PurchaseDTO.class);
}

}
