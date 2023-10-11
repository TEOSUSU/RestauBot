// DishDTO.java

package com.restaubot.spring.models.dto;

import org.springframework.stereotype.Component;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class DishDTO {
    private Integer idDish;
    private String name;
    private String description;
    private Double price;
    private String picture;
    private TypeDTO type;
    private RestaurantDTO restaurant;

    public DishDTO() {
    }

    public DishDTO(String name, String description, Double price, String picture, TypeDTO type, RestaurantDTO restaurant) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.picture = picture;
        this.type = type;
        this.restaurant = restaurant;
    }
}
