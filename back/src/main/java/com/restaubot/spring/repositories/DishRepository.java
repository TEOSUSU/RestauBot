package com.restaubot.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import com.restaubot.spring.models.entities.DishEntity;

public interface DishRepository extends JpaRepository<DishEntity, Integer>{
    @Query("SELECT d.name, d.price FROM Dish d JOIN d.dishPurchases dp WHERE dp.purchase.id = :purchaseId")
    List<DishEntity> findDishDetailsByPurchaseId(@Param("purchaseId") Integer purchaseId);

    @Query("SELECT d.name, d.price FROM Dish d JOIN d.dishMenus dm JOIN dm.menu.menuPurchases mp WHERE mp.purchase.id = :purchaseId")
    List<DishEntity> findDishDetailsByMenuPurchaseId(@Param("purchaseId") Integer purchaseId);
}
