package com.mikason.PropView.Exception.commercialEntityException;

public class FeatureAlreadyExistException extends RuntimeException {
    public FeatureAlreadyExistException() {
        super ("Feature already exist, fail to create feature");
    }
}
