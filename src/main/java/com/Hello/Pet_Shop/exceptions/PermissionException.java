package com.Hello.Pet_Shop.exceptions;

//This exception is for handling action that surpass the owner's action role
public class PermissionException extends RuntimeException
{
    public PermissionException(String message) {
        super(message);
    }
}
