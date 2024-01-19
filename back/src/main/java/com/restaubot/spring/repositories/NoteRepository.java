package com.restaubot.spring.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.restaubot.spring.models.entities.NoteEntity;

public interface NoteRepository extends JpaRepository<NoteEntity, Integer> {

    @Query("SELECT a FROM NoteEntity a WHERE a.restaurant.idUser = :id_Restaurant AND a.customer.idUser = :id_Customer")
    Optional<NoteEntity> findNoteByIdRestaurantAndCustomer(Integer id_Restaurant, Integer id_Customer);

    @Query("SELECT AVG(a.note) FROM NoteEntity a WHERE a.restaurant.idUser = :idRestaurant")
    Double findAverageNote(Integer idRestaurant);
}
