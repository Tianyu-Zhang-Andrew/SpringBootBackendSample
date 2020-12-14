package com.mikason.PropView.Exception.commercialEntityException;

public class SaleNotFoundException extends RuntimeException {
    public SaleNotFoundException() {
        super ("Could not find sale");
    }
}
