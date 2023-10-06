package com.restaubot.spring.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restaubot.spring.models.entities.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer>{

    Optional<CustomerEntity> findByMail(String mail);
}
