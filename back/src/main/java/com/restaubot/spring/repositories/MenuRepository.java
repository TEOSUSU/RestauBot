package com.restaubot.spring.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.restaubot.spring.models.entities.DishEntity;
import com.restaubot.spring.models.entities.MenuEntity;

public interface MenuRepository extends JpaRepository<MenuEntity, Integer>{

    @Query("SELECT m FROM MenuEntity m WHERE m.id = :id AND m.deleted = false")
    Optional<MenuEntity> findByIdAndDeletedFalse(Integer id);

    List<MenuEntity> findByDeletedFalse();
    
}
