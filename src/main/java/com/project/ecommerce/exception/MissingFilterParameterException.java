package com.project.ecommerce.exception;

public class MissingFilterParameterException extends RuntimeException {
    public MissingFilterParameterException(String parameterName)
    {
        super("Missing required parameter: " + parameterName +
                ". Please make sure to provide it in the request." +
                " example: /buyer/filter?sortBy=...");
    }

}
