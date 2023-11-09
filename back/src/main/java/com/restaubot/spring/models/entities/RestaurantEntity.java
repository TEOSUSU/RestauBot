package com.restaubot.spring.models.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
    
    @ManyToMany
    @JoinTable(name = "restaurant_slot",
                joinColumns = @JoinColumn(name="id_restaurant"),
                inverseJoinColumns = @JoinColumn(name="id_slot")
    )
    private Set<SlotEntity> assignedSlot = new HashSet<>();
    

}
