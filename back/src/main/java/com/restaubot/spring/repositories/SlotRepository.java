package com.restaubot.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restaubot.spring.models.entities.SlotEntity;

public interface SlotRepository extends JpaRepository<SlotEntity, Integer>{
    
}
