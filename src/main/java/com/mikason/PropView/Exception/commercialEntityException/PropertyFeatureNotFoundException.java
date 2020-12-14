package com.mikason.PropView.Exception.commercialEntityException;

public class PropertyFeatureNotFoundException extends RuntimeException {
    public PropertyFeatureNotFoundException() {
        super("Could not find propertyFeature");
    }
}
