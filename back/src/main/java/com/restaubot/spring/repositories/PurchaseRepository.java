package com.restaubot.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restaubot.spring.models.entities.PurchaseEntity;

public interface PurchaseRepository extends JpaRepository<PurchaseEntity, Integer> {
    
}
