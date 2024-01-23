package com.restaubot.spring.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restaubot.spring.models.dto.NoteDTO;
import com.restaubot.spring.models.entities.CustomerEntity;
import com.restaubot.spring.models.entities.NoteEntity;
import com.restaubot.spring.models.entities.RestaurantEntity;
import com.restaubot.spring.repositories.CustomerRepository;
import com.restaubot.spring.repositories.NoteRepository;
import com.restaubot.spring.repositories.RestaurantRepository;
import com.restaubot.spring.security.CustomRuntimeException;

@Service
@Transactional
public class NoteService {

    private static final Logger logger = LogManager.getLogger(NoteService.class);

    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private ModelMapper modelMapper;

    public NoteService(NoteRepository noteRepository2) {
    }

    public List<NoteDTO> findNoteByRestaurantAndCustomer(Integer idRestaurant, Integer idCustomer) throws CustomRuntimeException {
        try {
            List<NoteEntity> notes = noteRepository.findNoteByIdRestaurantAndCustomer(idCustomer, idRestaurant);
            return notes.stream()
                    .map(note -> modelMapper.map(note, NoteDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error retrieving purchases:", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }
    }

    public Double findAverageNoteByRestaurant(Integer idRestaurant) throws CustomRuntimeException {
        try {
            return noteRepository.findAverageNoteByRestaurantId(idRestaurant);
        } catch (Exception e) {
            logger.error("Error retrieving average note:", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }
    }
    

    public NoteDTO saveNote(NoteDTO note) throws CustomRuntimeException {
        NoteEntity noteEntity = modelMapper.map(note, NoteEntity.class);

        NoteEntity response = null;
        try {
            response = noteRepository.save(noteEntity);
        } catch (Exception e) {
            logger.error("Error saving customer:", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }
        return modelMapper.map(response, NoteDTO.class);
    }

    public NoteDTO createNote(NoteDTO noteDTO) throws CustomRuntimeException {
        NoteEntity noteEntity = modelMapper.map(noteDTO, NoteEntity.class);

        logger.info(noteEntity.toString());

        Integer customerId = noteDTO.getCustomer().getIdUser();
        Integer restaurantId = noteDTO.getRestaurant().getIdUser();

        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomRuntimeException(CustomRuntimeException.CUSTOMER_NOT_FOUND));

        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new CustomRuntimeException(CustomRuntimeException.CUSTOMER_NOT_FOUND));

        noteEntity.setCustomer(customer);
        noteEntity.setRestaurant(restaurant);

        NoteEntity response = null;
        try {
            response = noteRepository.save(noteEntity);
        } catch (Exception e) {
            logger.error("Error creating purchase:", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }

        return modelMapper.map(response, NoteDTO.class);
    }
}
