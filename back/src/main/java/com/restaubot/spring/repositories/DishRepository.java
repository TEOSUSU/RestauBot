package com.restaubot.spring.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.restaubot.spring.models.entities.DishEntity;

public interface DishRepository extends JpaRepository<DishEntity, Integer>, JpaSpecificationExecutor<DishEntity>{
    
    @Query("SELECT m FROM DishEntity m WHERE m.id = :id AND m.deleted = false")
    Optional<DishEntity> findByIdAndDeletedFalse(Integer id);

    List<DishEntity> findByDeletedFalse();

    @Query(value = "SELECT dish.* FROM dish JOIN dish_purchase ON dish.id_dish = dish_purchase.id_dish WHERE dish_purchase.id_purchase = :purchaseId", nativeQuery = true)
    List<DishEntity> findDishDetailsByPurchaseId(@Param("purchaseId") Integer purchaseId);

    @Query(value = "SELECT dish.* FROM dish JOIN dish_menu ON dish.id_dish = dish_menu.id_dish JOIN menu_purchase ON dish_menu.id_menu = menu_purchase.id_menu WHERE menu_purchase.id_purchase = :purchaseId", nativeQuery = true)
    List<DishEntity> findDishDetailsByMenuPurchaseId(@Param("purchaseId") Integer purchaseId);
}
