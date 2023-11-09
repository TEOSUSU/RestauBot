package com.restaubot.spring.security;

public class CustomRuntimeException extends Exception {

    // Default Messages
    public static final String DEFAULT_MESSAGE = "An error occured";
    public static final String SERVICE_ERROR = "Service Error";

    // Not found messages
    public static final String CUSTOMER_NOT_FOUND = "Client not found";

    // Client error messages
    public static final String CUSTOMER_ALREADY_EXISTS = "Client is already in the database";
    public static final String ID_CUSTOMER_SHOULD_BE_NULL = "Client id should be null";
    public static final String MAIL_TAKEN = "Client already exists with this mail";
    public static final String RESTAURANT_NOT_FOUND = "Restaurant not found";

    public CustomRuntimeException(String message) {
        super(message);
    }
}
