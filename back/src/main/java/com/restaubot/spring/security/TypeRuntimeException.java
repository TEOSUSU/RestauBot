package com.restaubot.spring.security;

public class TypeRuntimeException extends Exception{
    
    // Default Messages
    public static final String DEFAULT_MESSAGE = "An error occured";
    public static final String SERVICE_ERROR = "Service Error";

    // Not found messages
    public static final String TYPE_NOT_FOUND = "Type not found";

    // Client error messages
    public static final String TYPE_ALREADY_EXISTS = "Type is already in the database";

    public TypeRuntimeException(String message) {
        super(message);
    }
}
