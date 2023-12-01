package com.restaubot.spring.models.dto;

import org.springframework.stereotype.Component;

import com.restaubot.spring.security.CustomRuntimeException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class PersonDTO {
   private String login;
   private String password;
   private String role;

    // Constructeurs
    public PersonDTO() {
    }

    public PersonDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public Object thenThrow(CustomRuntimeException customRuntimeException) {
        return null;
    }

}
