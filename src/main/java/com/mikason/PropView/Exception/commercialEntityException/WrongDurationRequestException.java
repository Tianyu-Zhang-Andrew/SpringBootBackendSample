package com.mikason.PropView.Exception.commercialEntityException;

public class WrongDurationRequestException extends RuntimeException{
    public WrongDurationRequestException() {
        super("Both of start and end of the period must have value or be null.(now one has value and one is null)");
    }
}
