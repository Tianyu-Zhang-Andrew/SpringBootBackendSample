package com.mikason.PropView.Exception.commercialEntityException;

public class FeatureNotFoundException extends RuntimeException {
    public FeatureNotFoundException(Long id) {
        super ("Could not find feature " + id);
    }
}
