package com.restaubot.spring.models.entities;

import java.util.Collection;
import java.util.Collections;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customer")
public class CustomerEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCustomer;

    private String surname;
    private String firstname;
    private String mail;
    private String phone;
    private String address;
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleEntity role;

    public CustomerEntity() {
    }

    public CustomerEntity(String surname, String firstname, String mail, String phone, String address, String password, String role) {
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
return Collections.singleton( new SimpleGrantedAuthority( "ROLE_CUSTOMER" ) );

    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return this.mail;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
return true;
    }
}
