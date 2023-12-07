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
    private String picture;
    private String mail;
    private String password;
    private String color;
    private boolean fidelity;
    private boolean deleted;


    public RestaurantEntity() {
    }

    public RestaurantEntity(String companyName, String address, String zipcode, String city, String phone, String picture, String mail, String password, String color,boolean fidelity,boolean deleted) {
        this.companyName = companyName;
        this.address = address;
        this.zipcode = zipcode;
        this.city = city;
        this.phone = phone;
        this.picture = picture;
        this.mail = mail;
        this.password = password;
        this.fidelity = fidelity;
        this.deleted = deleted;
    }
    
    @ManyToMany
    @JoinTable(name = "restaurant_slot",
                joinColumns = @JoinColumn(name="id_restaurant"),
                inverseJoinColumns = @JoinColumn(name="id_slot")
    )
    private Set<SlotEntity> assignedSlot = new HashSet<>();
    


    @ManyToMany
    @JoinTable(name = "category_restaurant",
            joinColumns = @JoinColumn(name = "idRestaurant"),
            inverseJoinColumns = @JoinColumn(name = "idCategory")
    )
    private Set<CategoryEntity> assignedCategories = new HashSet<>();

    

    @ManyToMany
    @JoinTable(name = "type_restaurant",
            joinColumns = @JoinColumn(name = "idRestaurant"),
            inverseJoinColumns = @JoinColumn(name = "idType")
    )
    private Set<TypeEntity> assignedTypes = new HashSet<>();
}
