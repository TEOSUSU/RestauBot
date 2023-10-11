// DishEntity.java

package com.restaubot.spring.models.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "dish")
public class DishEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDish;

    private String name;
    private String description;
    private Double price;
    private String picture;

    @ManyToOne
    @JoinColumn(name = "id_type")
    private TypeEntity type;

    @ManyToOne
    @JoinColumn(name = "id_restaurant")
    private RestaurantEntity restaurant;

    public DishEntity() {
    }

    public DishEntity(String name, String description, Double price, String picture, TypeEntity type, RestaurantEntity restaurant) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.picture = picture;
        this.type = type;
        this.restaurant = restaurant;
    }
}
