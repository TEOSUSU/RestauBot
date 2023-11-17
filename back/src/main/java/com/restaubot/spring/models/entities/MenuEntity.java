// MenuEntity.java

package com.restaubot.spring.models.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;
import java.util.HashSet;

@Getter
@Setter
@Entity
@Table(name = "menu")
public class MenuEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMenu;

    private String name;
    private String description;
    private Double price;
    private String picture;

    @ManyToOne
    @JoinColumn(name = "id_restaurant")
    private RestaurantEntity restaurant;

    public MenuEntity() {
    }

    public MenuEntity(String name, String description, Double price, String picture, RestaurantEntity restaurant) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.picture = picture;
        this.restaurant = restaurant;
    }

    @ManyToMany
    @JoinTable(name = "dish_menu",
            joinColumns = @JoinColumn(name = "idMenu"),
            inverseJoinColumns = @JoinColumn(name = "idDish")
    )
    private Set<DishEntity> assignedDishes = new HashSet<>();
}
