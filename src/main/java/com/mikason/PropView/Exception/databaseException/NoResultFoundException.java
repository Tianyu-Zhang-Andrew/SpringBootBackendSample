package com.mikason.PropView.Exception.databaseException;

public class NoResultFoundException extends RuntimeException {
    public NoResultFoundException() {
        super ("No result is found");
    }
}
