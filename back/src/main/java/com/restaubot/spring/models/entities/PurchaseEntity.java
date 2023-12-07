// PurchaseEntity.java

package com.restaubot.spring.models.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "purchase")
public class PurchaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPurchase;

    private Double total;
    private boolean paid;
    private boolean collected;
    private LocalDate orderTime;
    private LocalDate collectTime;

    @ManyToOne
    @JoinColumn(name = "id_customer")
    private CustomerEntity customer;

    @ManyToMany
    @JoinTable(name = "dish_purchase",
                joinColumns = @JoinColumn(name="id_purchase"),
                inverseJoinColumns = @JoinColumn(name="id_dish")
    )
    private List<DishEntity> assignedDish = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "menu_purchase",
                joinColumns = @JoinColumn(name="id_purchase"),
                inverseJoinColumns = @JoinColumn(name="id_menu")
    )
    private List<MenuEntity> assignedMenu = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "id_restaurant")
    private RestaurantEntity restaurant;

    public PurchaseEntity() {
    }

    public PurchaseEntity(Double total, boolean paid, boolean collected, LocalDate orderTime, LocalDate collectTime, CustomerEntity customer, RestaurantEntity restaurant) {
        this.total = total;
        this.paid = paid;
        this.collected = collected;
        this.orderTime = orderTime;
        this.collectTime = collectTime;
        this.customer = customer;
        this.restaurant = restaurant;
    }
}
