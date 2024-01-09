package com.restaubot.spring.security;

public class CustomerDetailsResponse {
    private int id;

    private String typeUser;

    public CustomerDetailsResponse(int id, String typeUser){
        this.id = id;
        this.typeUser = typeUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(String typeUser) {
        this.typeUser = typeUser;
    }
}
