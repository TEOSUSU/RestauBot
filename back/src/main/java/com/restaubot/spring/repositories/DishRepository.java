package com.restaubot.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import com.restaubot.spring.models.entities.DishEntity;

public interface DishRepository extends JpaRepository<DishEntity, Integer> {
    // @Query("SELECT d FROM Dish d JOIN d.dishPurchases dp WHERE dp.purchase.id = :purchaseId")
    @Query(value = "SELECT dish.* FROM dish JOIN dish_purchase ON dish.id_dish = dish_purchase.id_dish WHERE dish_purchase.id_purchase = :purchaseId", nativeQuery = true)
    List<DishEntity> findDishDetailsByPurchaseId(@Param("purchaseId") Integer purchaseId);

    // @Query("SELECT d FROM Dish d JOIN d.dishMenus dm JOIN dm.menu.menuPurchases mp WHERE mp.purchase.id = :purchaseId")
    @Query(value = "SELECT dish.* FROM dish JOIN dish_menu ON dish.id_dish = dish_menu.id_dish JOIN menu_purchase ON dish_menu.id_menu = menu_purchase.id_menu WHERE menu_purchase.id_purchase = :purchaseId", nativeQuery = true)
    List<DishEntity> findDishDetailsByMenuPurchaseId(@Param("purchaseId") Integer purchaseId);

    // @Query("SELECT d.name, d.price FROM Dish d JOIN d.dishPurchases dp WHERE dp.purchase.id = :purchaseId")
    // List<DishEntity> findDishDetailsByPurchaseId(@Param("purchaseId") Integer purchaseId);

    // @Query("SELECT d.name, d.price FROM Dish d JOIN d.dishMenus dm JOIN dm.menu.menuPurchases mp WHERE mp.purchase.id = :purchaseId");
}
