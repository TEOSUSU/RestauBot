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
    private Integer idCustomer;
    private Integer idRestaurant;
    private Integer note;

    // Constructeurs
    public NoteDTO() {
    }

    public NoteDTO(Integer idAvis, Integer idCustomer, Integer idRestaurant, Integer note) {
        this.idAvis = idAvis;
        this.idCustomer = idCustomer;
        this.idRestaurant = idRestaurant;
        this.note = note;
    }

    public Object thenThrow(CustomRuntimeException customRuntimeException) {
        return null;
    }

    

}
