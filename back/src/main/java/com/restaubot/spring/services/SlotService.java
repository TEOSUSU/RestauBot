package com.restaubot.spring.services;

import java.time.LocalTime;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.weaver.patterns.ConcreteCflowPointcut.Slot;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restaubot.spring.models.dto.RestaurantDTO;
import com.restaubot.spring.models.dto.SlotDTO;
import com.restaubot.spring.models.entities.RestaurantEntity;
import com.restaubot.spring.models.entities.SlotEntity;
import com.restaubot.spring.repositories.RestaurantRepository;
import com.restaubot.spring.repositories.SlotRepository;
import com.restaubot.spring.security.CustomRuntimeException;

@Service
@Transactional
public class SlotService {

    private static final Logger logger = LogManager.getLogger(RestaurantService.class);

    @Autowired
    private SlotRepository slotRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private ModelMapper modelMapper;
    

   
    public SlotDTO createSlot(SlotDTO slotDTO) throws CustomRuntimeException {
        SlotEntity existingSlot = findExistingSlot(slotDTO);

        if (existingSlot != null) {
            return modelMapper.map(existingSlot, SlotDTO.class);
        } else {
            try {
                SlotEntity newSlot = saveSlot(slotDTO);
                return modelMapper.map(newSlot, SlotDTO.class);
            } catch (Exception e) {
                logger.error("Error creating or saving timeslot:", e);
                throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
            }
        }
    }

    public SlotEntity saveSlot(SlotDTO slot) throws CustomRuntimeException {
        SlotEntity slotEntity = modelMapper.map(slot, SlotEntity.class);

        try {
            return slotRepository.save(slotEntity);
        } catch (Exception e) {
            logger.error("Error saving restaurant:", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }
    }

    private SlotEntity findExistingSlot(SlotDTO slotDTO) {
        LocalTime startHour = slotDTO.getStartHour();
        LocalTime endHour = slotDTO.getEndHour();
        String day = slotDTO.getDay();

        return slotRepository.findByStartHourAndEndHourAndDay(startHour, endHour, day);
    }

    public List<SlotDTO> getSlotsByIdRestaurant(int id) throws CustomRuntimeException {
        try {
            // Récupérer le restaurant par son ID
            RestaurantEntity restaurant = restaurantRepository.findById(id)
                    .orElseThrow(() -> new CustomRuntimeException("Restaurant not found"));
    
            // Récupérer tous les slots associés à ce restaurant
            Set<SlotEntity> slots = restaurant.getAssignedSlot();
    
            // Mapper les slots en SlotDTO
            return slots.stream()
                    .map(slot -> modelMapper.map(slot, SlotDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching slots by restaurant ID:", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }
    }
    
}
