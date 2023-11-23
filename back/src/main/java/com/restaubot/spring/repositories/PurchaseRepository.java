package com.restaubot.spring.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.restaubot.spring.models.entities.PurchaseEntity;

public interface PurchaseRepository extends JpaRepository<PurchaseEntity, Long> {
    @Query("SELECT p FROM PurchaseEntity p " +
           "WHERE p.customer.id = :customerId AND p.restaurant.id = :restaurantId")
    List<PurchaseEntity> getPurchasesByCustomerIdAndRestaurantId(
        @Param("customerId") Integer customerId, @Param("restaurantId") Integer restaurantId);
    
    @Query("SELECT p FROM PurchaseEntity p " +
           "WHERE p.customer.id = :customerId")
    List<PurchaseEntity> getPurchasesByCustomerId(@Param("customerId") Integer customerId);

}

