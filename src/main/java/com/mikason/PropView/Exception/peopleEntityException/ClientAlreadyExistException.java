package com.mikason.PropView.Exception.peopleEntityException;

public class ClientAlreadyExistException extends RuntimeException {
    public ClientAlreadyExistException() {
        super ("Client already exist, fail to create client");
    }
}
