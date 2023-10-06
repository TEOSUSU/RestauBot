package com.restaubot.spring.models.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customer")
public class CustomerEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCustomer;

    private String surname;
    private String firstname;
    private String mail;
    private String phone;
    private String address;
    private String password;

    public CustomerEntity() {
    }

    public CustomerEntity(String surname, String firstname, String mail, String phone, String address, String password) {
        this.surname = surname;
        this.firstname = firstname;
        this.mail = mail;
        this.phone = phone;
        this.address = address;
        this.password = password;
    }
}
