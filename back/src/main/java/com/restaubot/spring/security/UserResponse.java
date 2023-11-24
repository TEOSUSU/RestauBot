package com.restaubot.spring.security;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponse {
    
    private String role;

    public UserResponse(String roleUser){
        this.role = roleUser;
    }

}
