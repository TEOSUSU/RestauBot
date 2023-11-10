package com.restaubot.spring.security;

public class DishRuntimeException extends Exception{
    
    // Default Messages
    public static final String DEFAULT_MESSAGE = "An error occured";
    public static final String SERVICE_ERROR = "Service Error";

    // Not found messages
    public static final String DISH_NOT_FOUND = "Dish not found";

    // Client error messages
    public static final String DISH_ALREADY_EXISTS = "Dish is already in the database";

    public DishRuntimeException(String message) {
        super(message);
    }
}
