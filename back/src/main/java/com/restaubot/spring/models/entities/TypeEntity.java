// TypeEntity.java

package com.restaubot.spring.models.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;
import java.util.HashSet;

@Getter
@Setter
@Entity
@Table(name = "type")
public class TypeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idType;

    private String name;

    @ManyToOne
    @JoinColumn(name = "id_category")
    private CategoryEntity category;

    public TypeEntity() {
    }

    public TypeEntity(String name, CategoryEntity category) {
        this.name = name;
        this.category = category;
    }

    @JsonIgnore
    @ManyToMany(mappedBy = "assignedTypes")
    private Set<RestaurantEntity> restaurantSet = new HashSet<>();
}
