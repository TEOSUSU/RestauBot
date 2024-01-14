package com.restaubot.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restaubot.spring.models.entities.NoteEntity;

public interface NoteRepository extends JpaRepository<NoteEntity, Integer>{    
    
}
