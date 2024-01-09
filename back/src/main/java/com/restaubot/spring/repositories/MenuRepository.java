package com.restaubot.spring.repositories;

import java.util.List;
import java.util.Optional;

// import org.hibernate.mapping.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.restaubot.spring.models.entities.MenuEntity;

public interface MenuRepository extends JpaRepository<MenuEntity, Integer>{

    @Query(value = "SELECT menu.* FROM menu JOIN menu_purchase ON menu.id_menu = menu_purchase.id_menu WHERE menu_purchase.id_purchase = :id_purchase", nativeQuery = true)
    List<MenuEntity> findMenuDetailsByPurchaseId(@Param("id_purchase") Integer id_purchase);
    
    @Query("SELECT m FROM MenuEntity m WHERE m.id = :id AND m.deleted = false")
    Optional<MenuEntity> findByIdAndDeletedFalse(Integer id);

    List<MenuEntity> findByDeletedFalse();
    
}
