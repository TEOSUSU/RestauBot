package com.restaubot.spring.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.restaubot.spring.models.entities.CategoryEntity;
import com.restaubot.spring.models.entities.TypeEntity;


public interface TypeRepository extends JpaRepository<TypeEntity, Integer>{
    
    List<TypeEntity> findByCategory(CategoryEntity category);
}
