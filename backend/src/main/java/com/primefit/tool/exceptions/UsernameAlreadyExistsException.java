package com.primefit.tool.exceptions;

public class UsernameAlreadyExistsException extends Exception{

    public UsernameAlreadyExistsException(String problem) {
        super(String.format("Username %s already exists!", problem));
    }
}
