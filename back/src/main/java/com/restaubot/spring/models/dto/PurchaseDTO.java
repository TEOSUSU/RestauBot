// PurchaseDTO.java

package com.restaubot.spring.models.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.restaubot.spring.models.entities.DishEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class PurchaseDTO {
    private Integer idPurchase;
    private Double total;
    private boolean paid;
    private boolean collected;
    private LocalDate orderTime;
    private LocalDate collectTime;
    private CustomerDTO customer;

    private List<DishEntity> assignedDish = new ArrayList<>();
    private RestaurantDTO restaurant;

    public PurchaseDTO() {
    }

    public PurchaseDTO(Double total, boolean paid, boolean collected, LocalDate orderTime, LocalDate collectTime, CustomerDTO customer, RestaurantDTO restaurant) {
        this.total = total;
        this.paid = paid;
        this.collected = collected;
        this.orderTime = orderTime;
        this.collectTime = collectTime;
        this.customer = customer;
        this.restaurant = restaurant;
    }


    @Override
    public String toString() {
        return "{" +
            " idPurchase='" + getIdPurchase() + "'" +
            ", total='" + getTotal() + "'" +
            ", paid='" + isPaid() + "'" +
            ", collected='" + isCollected() + "'" +
            ", orderTime='" + getOrderTime() + "'" +
            ", collectTime='" + getCollectTime() + "'" +
            ", customer='" + getCustomer() + "'" +
            ", assignedDish='" + getAssignedDish() + "'" +
            "}";
    }

}
