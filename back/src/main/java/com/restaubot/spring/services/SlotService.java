package com.restaubot.spring.services;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restaubot.spring.models.dto.SlotDTO;
import com.restaubot.spring.models.entities.SlotEntity;
import com.restaubot.spring.repositories.SlotRepository;
import com.restaubot.spring.security.CustomRuntimeException;

@Service
@Transactional
public class SlotService {

     private static final Logger logger = LogManager.getLogger(RestaurantService.class);

    @Autowired
    private SlotRepository slotRepository;
    @Autowired
    private ModelMapper modelMapper;
    
    public SlotDTO createSlot(SlotDTO slot) throws CustomRuntimeException {
        SlotEntity slotEntity = modelMapper.map(slot, SlotEntity.class);
        
        SlotEntity response = null;
        try {
            response = slotRepository.save(slotEntity);
        } catch (Exception e) {
            logger.error("Error creating timeslot:", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }

        return modelMapper.map(response, SlotDTO.class);
    }
}
