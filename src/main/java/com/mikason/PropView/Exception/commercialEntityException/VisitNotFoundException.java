package com.mikason.PropView.Exception.commercialEntityException;

public class VisitNotFoundException extends RuntimeException {
    public VisitNotFoundException() {
        super("Could not find visit");
    }
}