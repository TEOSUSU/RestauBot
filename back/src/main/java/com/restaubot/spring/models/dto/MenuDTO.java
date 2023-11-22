// MenuDTO.java

package com.restaubot.spring.models.dto;
import java.util.Set;
import org.springframework.stereotype.Component;

import com.restaubot.spring.models.entities.DishEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class MenuDTO {
    private Integer idMenu;
    private String name;
    private String description;
    private Double price;
    private String picture;
    private RestaurantDTO restaurant;
    private Set<DishEntity> assignedDishes;

    public MenuDTO() {
    }

    public MenuDTO(String name, String description, Double price, String picture, RestaurantDTO restaurant) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.picture = picture;
        this.restaurant = restaurant;
    }
}
