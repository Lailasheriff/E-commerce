package com.project.ecommerce.exception;


public class MissingSearchParameterException extends RuntimeException {
    public MissingSearchParameterException(String parameterName) {
        super("Missing required parameter: " + parameterName +
                ". Please make sure to provide it in the request."+
                " example: /buyer/search?query=...");
    }
}

