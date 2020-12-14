package com.mikason.PropView.Exception.documentEntityException;

public class RentalAgreementAlreadyExistException extends RuntimeException {
    public RentalAgreementAlreadyExistException() {
        super ("RentalAgreement already exist, fail to create RentalAgreement");
    }
}
