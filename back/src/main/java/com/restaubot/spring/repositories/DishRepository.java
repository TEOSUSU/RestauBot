package com.restaubot.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restaubot.spring.models.entities.DishEntity;

public interface DishRepository extends JpaRepository<DishEntity, Integer>{
    
}
