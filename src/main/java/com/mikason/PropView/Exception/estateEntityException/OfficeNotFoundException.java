package com.mikason.PropView.Exception.estateEntityException;

public class OfficeNotFoundException extends RuntimeException {
    public OfficeNotFoundException(Long id) {
        super ("Could not find office " + id);
    }
}
