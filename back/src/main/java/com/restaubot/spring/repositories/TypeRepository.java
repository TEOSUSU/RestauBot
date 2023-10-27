package com.restaubot.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restaubot.spring.models.entities.TypeEntity;

public interface TypeRepository extends JpaRepository<TypeEntity, Integer>{
    
}