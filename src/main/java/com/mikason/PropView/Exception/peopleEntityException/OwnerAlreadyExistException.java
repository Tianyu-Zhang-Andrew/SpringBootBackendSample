package com.mikason.PropView.Exception.peopleEntityException;

public class OwnerAlreadyExistException extends RuntimeException {
    public OwnerAlreadyExistException() {
        super ("Owner already exist, fail to create property");
    }
}
