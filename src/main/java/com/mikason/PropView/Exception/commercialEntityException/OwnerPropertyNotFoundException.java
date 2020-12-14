package com.mikason.PropView.Exception.commercialEntityException;

public class OwnerPropertyNotFoundException extends RuntimeException {
    public OwnerPropertyNotFoundException() {
        super ("Could not find ownerProperty");
    }
}
