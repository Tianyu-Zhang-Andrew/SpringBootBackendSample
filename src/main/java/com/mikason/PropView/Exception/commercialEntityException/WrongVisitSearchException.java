package com.mikason.PropView.Exception.commercialEntityException;

public class WrongVisitSearchException extends RuntimeException{
    public WrongVisitSearchException() {
        super("Your visit value is null, now is searching all visit during a period, start and end value must all have value");
    }
}
