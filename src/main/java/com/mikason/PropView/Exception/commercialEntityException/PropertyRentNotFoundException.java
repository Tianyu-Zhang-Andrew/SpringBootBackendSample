package com.mikason.PropView.Exception.commercialEntityException;

public class PropertyRentNotFoundException extends RuntimeException {
    public PropertyRentNotFoundException(Long id) {
        super ("Could not find propertyRent " + id);
    }
}
