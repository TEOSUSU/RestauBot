package com.restaubot.spring.models.dto;

import org.springframework.stereotype.Component;
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
    private String mail;
    private boolean fidelity;

    public RestaurantDTO() {
    }

    public RestaurantDTO(String companyName, String address, String zipcode, String city, String phone, String mail, boolean fidelity) {
        this.companyName = companyName;
        this.address = address;
        this.zipcode = zipcode;
        this.city = city;
        this.phone = phone;
        this.mail = mail;
        this.fidelity = fidelity;
    }
}
