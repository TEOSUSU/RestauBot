package com.restaubot.spring.models.dto;

import org.springframework.stereotype.Component;

import com.restaubot.spring.security.CustomRuntimeException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class CustomerDTO {
    private Integer idCustomer;
    private String surname;
    private String firstname;
    private String mail;
    private String phone;
    private String address;
    private String password;

    // Constructeurs
    public CustomerDTO() {
    }

    public CustomerDTO(String surname, String firstname, String mail, String phone, String address, String password) {
        this.surname = surname;
        this.firstname = firstname;
        this.mail = mail;
        this.phone = phone;
        this.address = address;
        this.password = password;
    }

    public Object thenThrow(CustomRuntimeException customRuntimeException) {
        return null;
    }

    

}
