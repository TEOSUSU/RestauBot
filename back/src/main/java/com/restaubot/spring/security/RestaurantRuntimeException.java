package com.restaubot.spring.security;

public class RestaurantRuntimeException extends Exception{
    
    // Default Messages
    public static final String DEFAULT_MESSAGE = "An error occured";
    public static final String SERVICE_ERROR = "Service Error";

    // Not found messages
    public static final String RESTAURANT_NOT_FOUND = "Restaurant not found";

    // Client error messages
    public static final String RESTAURANT_ALREADY_EXISTS = "Restaurant is already in the database";

    public RestaurantRuntimeException(String message) {
        super(message);
    }
}
