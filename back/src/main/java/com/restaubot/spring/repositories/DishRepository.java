package com.restaubot.spring.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.restaubot.spring.models.entities.DishEntity;

public interface DishRepository extends JpaRepository<DishEntity, Integer>, JpaSpecificationExecutor<DishEntity>{
    
    @Query("SELECT m FROM MenuEntity m WHERE m.id = :id AND m.deleted = false")
    Optional<DishEntity> findByIdAndDeletedFalse(Integer id);

    List<DishEntity> findByDeletedFalse();
}
