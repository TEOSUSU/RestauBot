package com.restaubot.spring.security;

public class CustomerDetailsResponse {
    private int id;

    private String typePerson;

    public CustomerDetailsResponse(int id, String typePerson){
        this.id = id;
        this.typePerson = typePerson;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypePerson() {
        return typePerson;
    }

    public void setTypePerson(String typePerson) {
        this.typePerson = typePerson;
    }
}
