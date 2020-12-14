package com.mikason.PropView.Exception.commercialEntityException;

public class RentAlreadyExistException extends RuntimeException {
    public RentAlreadyExistException() {
        super ("Rent already exist, fail to create sale");
    }
}
