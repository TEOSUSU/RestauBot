package com.restaubot.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restaubot.spring.models.entities.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer>{
    
}
