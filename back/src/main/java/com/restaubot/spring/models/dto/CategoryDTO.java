// CategoryDTO.java

package com.restaubot.spring.models.dto;

import org.springframework.stereotype.Component;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class CategoryDTO {
    private Integer idCategory;
    private String name;

    public CategoryDTO() {
    }

    public CategoryDTO(String name) {
        this.name = name;
    }
}
