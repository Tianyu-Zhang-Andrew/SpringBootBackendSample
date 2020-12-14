package com.mikason.PropView.Exception.commercialEntityException;

public class VisitAlreadyExistException extends RuntimeException {
    public VisitAlreadyExistException() {
        super ("Visit already exist, fail to create visit");
    }
}
