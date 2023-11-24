package com.restaubot.spring.repositories;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restaubot.spring.models.entities.SlotEntity;

public interface SlotRepository extends JpaRepository<SlotEntity, Integer> {

    SlotEntity findByStartHourAndEndHourAndDay(LocalTime startHour, LocalTime endHour, String day);

}
