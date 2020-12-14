package com.mikason.PropView.Exception.documentEntityException;

public class SaleDocumentAlreadyExistException extends RuntimeException {
    public SaleDocumentAlreadyExistException() {
        super ("SaleDocument already exist, fail to create RentalAgreement");
    }
}
