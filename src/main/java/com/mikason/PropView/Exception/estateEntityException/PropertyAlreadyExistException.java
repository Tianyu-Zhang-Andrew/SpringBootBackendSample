package com.mikason.PropView.Exception.estateEntityException;

public class PropertyAlreadyExistException extends RuntimeException {
    public PropertyAlreadyExistException() {
        super ("Property already exist, fail to create property");
    }
}
