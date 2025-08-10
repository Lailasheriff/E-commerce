package com.project.ecommerce.exception;

public class AccessDeniedForBuyerException extends RuntimeException {
    public AccessDeniedForBuyerException() {
        super("Access denied for non-buyer users");
    }
}
