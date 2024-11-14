package com.Hello.Pet_Shop.exceptions;

public class CustomBadCredentialsException extends RuntimeException {

    public CustomBadCredentialsException(String message) {
        super(message);
    }
}

