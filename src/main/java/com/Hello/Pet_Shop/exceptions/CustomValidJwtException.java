package com.Hello.Pet_Shop.exceptions;

public class CustomValidJwtException extends RuntimeException
{
    public CustomValidJwtException(String message) {
        super(message);
    }
}
