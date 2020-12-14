package com.mikason.PropView.Exception.commercialEntityException;

public class AgentAlreadyExistException extends RuntimeException {
    public AgentAlreadyExistException() {
        super ("Agent already exist, fail to create agent");
    }
}
