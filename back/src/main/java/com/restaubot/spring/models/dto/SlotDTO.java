package com.restaubot.spring.models.dto;

import org.springframework.stereotype.Component;

import com.restaubot.spring.models.entities.RestaurantEntity;
import java.time.LocalTime;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class SlotDTO {
    //All the possibilities for the enum 'day'
    public static final String MONDAY = "MONDAY";
    public static final String TUESDAY = "TUESDAY";
    public static final String WENESDAY = "WENESDAY";
    public static final String THURDAY = "THURDAY";
    public static final String FRIDAY = "FRIDAY";
    public static final String SATURDAY = "SATURDAY";
    public static final String SUNDAY = "SUNDAY";

    private Integer idSlot;
    private String day;
    private LocalTime startHour;
    private LocalTime endHour;

    private Set<RestaurantEntity> restaurantsSet;

    public SlotDTO() {
    }

    public SlotDTO(String day, LocalTime startHour, LocalTime endHour) {
        this.day = day;
        this.startHour = startHour;
        this.endHour = endHour;
    }
}
