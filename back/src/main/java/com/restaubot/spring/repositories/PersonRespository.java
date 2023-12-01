package com.restaubot.spring.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restaubot.spring.models.entities.PersonEntity;

public interface PersonRespository extends JpaRepository<PersonEntity, String>{
    
        Optional<PersonEntity> findByMail(String mail);

}
