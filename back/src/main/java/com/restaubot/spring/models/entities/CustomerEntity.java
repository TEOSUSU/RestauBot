package com.restaubot.spring.models.entities;



import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customer")
@DiscriminatorValue("Customer")
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity extends UserEntity {
    private String surname;
    private String firstname;
    private String phone;
    private String address;
}
