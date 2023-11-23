// TypeDTO.java

package com.restaubot.spring.models.dto;

import org.springframework.stereotype.Component;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
@Component
public class TypeDTO {
    private Integer idType;
    private String name;
    private CategoryDTO category;
    private Set<RestaurantDTO> restaurantSet;

    public TypeDTO() {
    }

    public TypeDTO(String name, CategoryDTO category) {
        this.name = name;
        this.category = category;
    }
}
