package com.restaubot.spring.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restaubot.spring.models.entities.RestaurantEntity;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Integer>{
    Optional<RestaurantEntity> findByMail(String mail);
}
