package com.mikason.PropView.Exception.commercialEntityException;

public class PropertyFeatureAlreadyExistException extends RuntimeException {
    public PropertyFeatureAlreadyExistException() {
        super("PropertyFeature already exist, fail to create propertyFeature");
    }
}
