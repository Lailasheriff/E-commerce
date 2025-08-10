package com.project.ecommerce.exception;

public class UnauthorizedProductAccessException extends RuntimeException {
    public UnauthorizedProductAccessException() {
        super("You are not authorized to perform this action on this product");
    }
}