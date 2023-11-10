// CategoryDTO.java

package com.restaubot.spring.models.dto;

import org.springframework.stereotype.Component;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
@Component
public class CategoryDTO {
    private Integer idCategory;
    private String name;
    private Set<RestaurantDTO> restaurantSet;

    public CategoryDTO() {
    }

    public CategoryDTO(String name) {
        this.name = name;
    }
}
