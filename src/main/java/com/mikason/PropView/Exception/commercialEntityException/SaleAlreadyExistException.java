package com.mikason.PropView.Exception.commercialEntityException;

public class SaleAlreadyExistException extends RuntimeException {
    public SaleAlreadyExistException() {
        super ("Sale already exist, fail to create sale");
    }
}
