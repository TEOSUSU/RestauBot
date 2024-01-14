package com.restaubot.spring.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.restaubot.spring.models.dto.CustomerDTO;
import com.restaubot.spring.models.dto.NoteDTO;
import com.restaubot.spring.models.entities.CustomerEntity;
import com.restaubot.spring.models.entities.NoteEntity;
import com.restaubot.spring.repositories.CustomerRepository;
import com.restaubot.spring.repositories.NoteRepository;
import com.restaubot.spring.security.CustomRuntimeException;

@Service
@Transactional
public class NoteService {

    private static final Logger logger = LogManager.getLogger(CustomerService.class);

    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private ModelMapper modelMapper;

    public NoteService(NoteRepository noteRepository2) {
    }


    /*public CustomerDTO getNoteById(Integer id) throws CustomRuntimeException {
        Optional<CustomerEntity> optionalCustomer = Optional.empty();
        try {
            optionalCustomer = noteRepository.findById(id);
        } catch (Exception e) {
            logger.error("Error findById", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }
        if (optionalCustomer.isEmpty()) {
            throw new CustomRuntimeException(CustomRuntimeException.CUSTOMER_NOT_FOUND);
        }
        return modelMapper.map(optionalCustomer.get(), CustomerDTO.class);
    }*/

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
        Optional<NoteEntity> optionalNote = Optional.empty();
        NoteDTO note = new NoteDTO();
        try {
            optionalNote = customerRepository.findByMail(customerDTO.getMail());
        } catch (Exception e) {
            logger.error("Error findByLogin", e);
            throw new CustomRuntimeException(CustomRuntimeException.SERVICE_ERROR);
        }
        if (optionalCustomer.isEmpty()) {
            saveCustomer(customer);
        }else{
            throw new CustomRuntimeException(CustomRuntimeException.MAIL_TAKEN);
        }
        return customer;
    }
}
