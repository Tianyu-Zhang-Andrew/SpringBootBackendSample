package com.mikason.PropView.Exception.estateEntityException;

public class PropertyNotFoundException extends RuntimeException {
    public PropertyNotFoundException(Long id) {
        super ("Could not find property " + id);
    }
}
