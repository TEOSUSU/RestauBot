package com.restaubot.spring.models.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.transaction.Transactional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;
import java.util.HashSet;

@Getter
@Setter
@Entity
@Transactional
@Table(name = "restaurant")
@DiscriminatorValue("Restaurant")
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantEntity extends PersonEntity implements Serializable {
    private String companyName;
    private String address;
    private String zipcode;
    private String city;
    private String phone;
    private String picture;
    private boolean fidelity;

    @ManyToMany
    @JoinTable(name = "restaurant_slot", joinColumns = @JoinColumn(name = "idPerson"), inverseJoinColumns = @JoinColumn(name = "id_slot"))
    private Set<SlotEntity> assignedSlot = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "category_restaurant", joinColumns = @JoinColumn(name = "idPerson"), inverseJoinColumns = @JoinColumn(name = "idCategory"))
    private Set<CategoryEntity> assignedCategories = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "type_restaurant", joinColumns = @JoinColumn(name = "idPerson"), inverseJoinColumns = @JoinColumn(name = "idType"))
    private Set<TypeEntity> assignedTypes = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_RESTAURANT"));
    }
}
