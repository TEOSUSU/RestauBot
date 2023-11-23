package com.restaubot.spring.security;

//Cette classe permet de créer les informations renvoyées après le login d'une personne sur le site

public class AuthResponse {

    private String mail;
    private String accessToken;
    private String userType;

    public AuthResponse() {}

    public AuthResponse(String mail, String accessToken, String userType) {
        this.mail = mail;
        this.accessToken = accessToken;
        this.userType = userType;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
