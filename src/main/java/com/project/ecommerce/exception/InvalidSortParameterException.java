package com.project.ecommerce.exception;

public class InvalidSortParameterException extends RuntimeException {
    public InvalidSortParameterException() {
        super("Invalid sortBy parameters");
    }
}