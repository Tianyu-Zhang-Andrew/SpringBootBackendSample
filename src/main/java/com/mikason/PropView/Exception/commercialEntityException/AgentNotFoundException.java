package com.mikason.PropView.Exception.commercialEntityException;

public class AgentNotFoundException extends RuntimeException {
    public AgentNotFoundException(Long id) {
        super ("Could not find agent " + id);
    }
}
