package com.restaubot.spring.models.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Integer idUser;
    private String mail;
    private String role;
}
