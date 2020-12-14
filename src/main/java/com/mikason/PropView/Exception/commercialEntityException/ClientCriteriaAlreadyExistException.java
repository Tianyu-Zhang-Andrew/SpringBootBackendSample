package com.mikason.PropView.Exception.commercialEntityException;

public class ClientCriteriaAlreadyExistException extends RuntimeException {
    public ClientCriteriaAlreadyExistException() {
        super ("ClientCriteria already exist, fail to create feature");
    }
}
