package com.restaubot.spring.models.dto;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.restaubot.spring.models.entities.SlotEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class RestaurantDTO {
    private Integer idRestaurant;
    private String companyName;
    private String address;
    private String zipcode;
    private String city;
    private String phone;
    private String picture;
    private String mail;
    private String password;
    private boolean fidelity;

    private Set<SlotEntity> assignedSlot;
    
    public RestaurantDTO() {
    }

    public RestaurantDTO(String companyName, String address, String zipcode, String city, String phone, String picture, String mail, String password, boolean fidelity) {
        this.companyName = companyName;
        this.address = address;
        this.zipcode = zipcode;
        this.city = city;
        this.phone = phone;
        this.picture = picture;
        this.mail = mail;
        this.password = password;
        this.fidelity = fidelity;
    }
}
