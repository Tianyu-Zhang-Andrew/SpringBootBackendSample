package com.mikason.PropView.Exception.estateEntityException;

public class OfficeAlreadyExistException extends RuntimeException {
    public OfficeAlreadyExistException() {
        super ("Office already exist, fail to create feature");
    }
}
