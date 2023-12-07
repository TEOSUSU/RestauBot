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
    private String password;
    private String mail;
    private String role;

    private Set<SlotEntity> assignedSlot;
    
    public RestaurantDTO() {
    }

    public RestaurantDTO(Integer idRestaurant, String companyName, String address, String zipcode, String city, String phone, String picture, boolean fidelity, String password, String mail, String role) {
        this.idRestaurant = idRestaurant;
        this.companyName = companyName;
        this.address = address;
        this.zipcode = zipcode;
        this.city = city;
        this.phone = phone;
        this.picture = picture;
        this.fidelity = fidelity;
        this.password = password;
        this.mail = mail;
        this.role = role;
    }

    public Integer getIdRestaurant() {
        return idRestaurant;
    }

    public void setIdRestaurant(Integer idRestaurant) {
        this.idRestaurant = idRestaurant;
    }


}
