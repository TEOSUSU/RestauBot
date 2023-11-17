package com.restaubot.spring.security;

public class CategoryRuntimeException extends Exception{
    
    // Default Messages
    public static final String DEFAULT_MESSAGE = "An error occured";
    public static final String SERVICE_ERROR = "Service Error";

    // Not found messages
    public static final String CATEGORY_NOT_FOUND = "Category not found";

    // Client error messages
    public static final String CATEGORY_ALREADY_EXISTS = "Category is already in the database";

    public CategoryRuntimeException(String message) {
        super(message);
    }
}
