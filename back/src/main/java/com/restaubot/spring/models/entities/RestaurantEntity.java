package com.restaubot.spring.models.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "restaurant")
public class RestaurantEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRestaurant;

    private String companyName;
    private String address;
    private String zipcode;
    private String city;
    private String phone;
    private String mail;
    private boolean fidelity;

    public RestaurantEntity() {
    }

    public RestaurantEntity(String companyName, String address, String zipcode, String city, String phone, String mail, boolean fidelity) {
        this.companyName = companyName;
        this.address = address;
        this.zipcode = zipcode;
        this.city = city;
        this.phone = phone;
        this.mail = mail;
        this.fidelity = fidelity;
    }
}
