package com.restaubot.spring.models.entities;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.transaction.Transactional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@Transactional
@Table(name = "restaurant")
@DiscriminatorValue("Restaurant")
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantEntity extends UserEntity {
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


    @ManyToMany
    @JoinTable(name = "restaurant_slot", joinColumns = @JoinColumn(name = "idUser"), inverseJoinColumns = @JoinColumn(name = "id_slot"))
    private Set<SlotEntity> assignedSlot = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "category_restaurant", joinColumns = @JoinColumn(name = "idUser"), inverseJoinColumns = @JoinColumn(name = "idCategory"))
    private Set<CategoryEntity> assignedCategories = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "type_restaurant", joinColumns = @JoinColumn(name = "idUser"), inverseJoinColumns = @JoinColumn(name = "idType"))
    private Set<TypeEntity> assignedTypes = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_RESTAURANT"));
    }
}
