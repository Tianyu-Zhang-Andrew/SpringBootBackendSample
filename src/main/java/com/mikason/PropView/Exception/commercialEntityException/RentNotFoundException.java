package com.mikason.PropView.Exception.commercialEntityException;

public class RentNotFoundException extends RuntimeException {
    public RentNotFoundException() {
        super ("Could not find Rent");
    }
}
