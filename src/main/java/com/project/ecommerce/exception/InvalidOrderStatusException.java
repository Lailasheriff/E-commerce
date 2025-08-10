package com.project.ecommerce.exception;

public class InvalidOrderStatusException extends RuntimeException{

    public InvalidOrderStatusException(String message){
        super(message);
    }
}
