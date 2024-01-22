package com.restaubot.spring.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.restaubot.spring.models.entities.NoteEntity;

public interface NoteRepository extends JpaRepository<NoteEntity, Integer> {

    @Query("SELECT a FROM NoteEntity a"
            + " WHERE a.restaurant.idUser = :id_restaurant AND a.customer.idUser = :id_customer")
    List<NoteEntity> findNoteByIdRestaurantAndCustomer(@Param("id_customer") Integer customerId,
            @Param("id_restaurant") Integer restaurantId);

    @Query("SELECT AVG(a.note) FROM NoteEntity a" +
            " WHERE a.restaurant.idUser = :id_restaurant")
    Double findAverageNoteByRestaurantId(@Param("id_restaurant") Integer restaurantId);

}
