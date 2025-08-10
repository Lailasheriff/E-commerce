package com.project.ecommerce.exception;

public class MissingAuthorizationHeaderException extends RuntimeException {
    public MissingAuthorizationHeaderException() {
        super("Missing or invalid Authorization header");
    }
}
