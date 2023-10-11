// PurchaseEntity.java

package com.restaubot.spring.models.entities;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
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

    @ManyToOne
    @JoinColumn(name = "id_menu")
    private MenuEntity menu;

    public PurchaseEntity() {
    }

    public PurchaseEntity(Double total, boolean paid, boolean collected, LocalDate orderTime, LocalDate collectTime, CustomerEntity customer, MenuEntity menu) {
        this.total = total;
        this.paid = paid;
        this.collected = collected;
        this.orderTime = orderTime;
        this.collectTime = collectTime;
        this.customer = customer;
        this.menu = menu;
    }
}
