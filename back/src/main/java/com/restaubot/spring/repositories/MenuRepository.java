package com.restaubot.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restaubot.spring.models.entities.MenuEntity;

public interface MenuRepository extends JpaRepository<MenuEntity, Integer>{
    
}
