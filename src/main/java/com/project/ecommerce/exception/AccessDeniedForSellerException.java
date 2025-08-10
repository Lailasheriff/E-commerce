package com.project.ecommerce.exception;

public class AccessDeniedForSellerException extends RuntimeException {
    public AccessDeniedForSellerException() {
        super("Access denied for non-seller users");
    }
}
