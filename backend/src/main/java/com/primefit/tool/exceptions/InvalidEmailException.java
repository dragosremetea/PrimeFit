package com.primefit.tool.exceptions;

public class InvalidEmailException extends Exception{

    public InvalidEmailException(String problem) {
        super(String.format("Email %s is not valid!", problem));
    }
}
