package com.mikason.PropView.Exception.commercialEntityException;

public class VisitElementLostException extends RuntimeException {
    public VisitElementLostException() {
        super ("Visit element lost, a visit must have property, agent, client");
    }
}
