package com.mikason.PropView.Exception.peopleEntityException;

public class OwnerNotFoundException extends RuntimeException {
    public OwnerNotFoundException(Long id) {
        super ("Could not find owner " + id);
    }
}
