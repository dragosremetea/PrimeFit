package com.primefit.tool.exceptions;

public class EmailAlreadyExistsException extends Exception{
    public EmailAlreadyExistsException(String problem) {
        super(String.format("Email %s already exists!", problem));
    }
}
