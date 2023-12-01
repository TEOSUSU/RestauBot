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
    private boolean fidelity;

    private Set<SlotEntity> assignedSlot;
    
    public RestaurantDTO() {
    }

    public RestaurantDTO(Integer idRestaurant, String companyName, String address, String zipcode, String city, String phone, String picture, boolean fidelity) {
        this.idRestaurant = idRestaurant;
        this.companyName = companyName;
        this.address = address;
        this.zipcode = zipcode;
        this.city = city;
        this.phone = phone;
        this.picture = picture;
        this.fidelity = fidelity;
    }

    public Integer getIdRestaurant() {
        return idRestaurant;
    }

    public void setIdRestaurant(Integer idRestaurant) {
        this.idRestaurant = idRestaurant;
    }


}
