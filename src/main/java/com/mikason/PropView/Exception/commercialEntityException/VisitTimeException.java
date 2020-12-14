package com.mikason.PropView.Exception.commercialEntityException;

public class VisitTimeException extends RuntimeException {
    public VisitTimeException() {
        super ("The start time for your visit is latter than the end time");
    }
}
