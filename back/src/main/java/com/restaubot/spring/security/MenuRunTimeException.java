package com.restaubot.spring.security;

public class MenuRunTimeException extends Exception{
    
    // Default Messages
    public static final String DEFAULT_MESSAGE = "An error occured";
    public static final String SERVICE_ERROR = "Service Error";

    // Not found messages
    public static final String MENU_NOT_FOUND = "Menu not found";

    // Client error messages
    public static final String MENU_ALREADY_EXISTS = "Menu is already in the database";

    public MenuRunTimeException(String message) {
        super(message);
    }
}