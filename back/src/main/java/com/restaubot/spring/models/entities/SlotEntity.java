package com.restaubot.spring.models.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "slot")
public class SlotEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSlot;

    private String day;
    private LocalTime startHour;
    private LocalTime endHour;

    public SlotEntity() {
    }

    public SlotEntity(String day, LocalTime startHour, LocalTime endHour) {
        this.day = day;
        this.startHour = startHour;
        this.endHour = endHour;
    }
}
