package com.mikason.PropView.Exception.documentEntityException;

public class SaleDocumentNotFoundException extends RuntimeException {
    public SaleDocumentNotFoundException(Long id) {
        super ("Could not find saleDocument " + id);
    }
}
