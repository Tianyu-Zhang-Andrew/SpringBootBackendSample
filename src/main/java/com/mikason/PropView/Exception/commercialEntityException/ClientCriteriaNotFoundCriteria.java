package com.mikason.PropView.Exception.commercialEntityException;

public class ClientCriteriaNotFoundCriteria extends RuntimeException {
    public ClientCriteriaNotFoundCriteria() {
        super ("Could not find clientCriteria");
    }
}
