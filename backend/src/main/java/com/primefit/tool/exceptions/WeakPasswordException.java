package com.primefit.tool.exceptions;

public class WeakPasswordException extends Exception {

    public WeakPasswordException(String problem) {
        super(String.format("Password does not contain at least %s !", problem));
    }
}