package com.restaubot.spring.models.entities;

import java.util.Collection;
import java.util.Collections;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customer")
@DiscriminatorValue("Customer")
public class CustomerEntity extends PersonEntity {
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
    }
}
