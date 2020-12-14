package com.mikason.PropView.Exception.commercialEntityException;

public class PropertySaleNotFoundException extends RuntimeException {
    public PropertySaleNotFoundException(Long id) {
        super ("Could not find propertySale " + id);
    }
}
