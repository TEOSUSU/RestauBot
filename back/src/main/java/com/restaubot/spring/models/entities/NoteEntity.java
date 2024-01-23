package com.restaubot.spring.models.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "avis")
public class NoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAvis;

    @ManyToOne
    @JoinColumn(name = "id_customer")
    private CustomerEntity customer;

    @ManyToOne
    @JoinColumn(name = "id_restaurant")
    private RestaurantEntity restaurant;

    
    private Integer note;

    public NoteEntity() {
    }

    public NoteEntity(Integer idAvis, CustomerEntity customer, RestaurantEntity restaurant, Integer note) {
        this.idAvis = idAvis;
        this.customer = customer;
        this.restaurant = restaurant;
        this.note = note;
    }
}
