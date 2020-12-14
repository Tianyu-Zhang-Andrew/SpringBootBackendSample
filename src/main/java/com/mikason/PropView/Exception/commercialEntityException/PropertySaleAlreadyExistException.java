package com.mikason.PropView.Exception.commercialEntityException;

public class PropertySaleAlreadyExistException extends RuntimeException {
    public PropertySaleAlreadyExistException() {
        super ("PropertySale already exist, fail to create propertySale");
    }
}
