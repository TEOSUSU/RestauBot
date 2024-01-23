package com.restaubot.spring.models.dto;

import org.springframework.stereotype.Component;

import com.restaubot.spring.security.CustomRuntimeException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class NoteDTO {
    private Integer idAvis;
    private CustomerDTO customer;
    private RestaurantDTO restaurant;
    private Integer note;

    // Constructeurs
    public NoteDTO() {
    }

    public NoteDTO(Integer idAvis, CustomerDTO customer, RestaurantDTO restaurant, Integer note) {
        this.idAvis = idAvis;
        this.customer = customer;
        this.restaurant = restaurant;
        this.note = note;
    }

    public Object thenThrow(CustomRuntimeException customRuntimeException) {
        return null;
    }

    

}
