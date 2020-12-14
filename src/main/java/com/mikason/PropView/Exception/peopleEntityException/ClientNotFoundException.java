package com.mikason.PropView.Exception.peopleEntityException;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(Long id) {
        super ("Could not find client " + id);
    }
}
