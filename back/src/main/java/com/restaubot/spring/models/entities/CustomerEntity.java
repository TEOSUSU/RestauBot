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
