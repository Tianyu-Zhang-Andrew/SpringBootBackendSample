package com.mikason.PropView.Exception.commercialEntityException;

public class PropertyRentAlreadyExistException extends RuntimeException{
    public PropertyRentAlreadyExistException() {
        super ("PropertyRent already exist, fail to create propertySale");
    }
}
