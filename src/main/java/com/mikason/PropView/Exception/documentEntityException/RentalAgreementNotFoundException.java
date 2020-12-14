package com.mikason.PropView.Exception.documentEntityException;

public class RentalAgreementNotFoundException extends RuntimeException {
    public RentalAgreementNotFoundException(Long id) {
        super ("Could not find rentalAgreement " + id);
    }
}
