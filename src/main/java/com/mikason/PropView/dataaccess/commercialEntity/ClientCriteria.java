package com.mikason.PropView.dataaccess.commercialEntity;

import com.mikason.PropView.dataaccess.compositeKey.ClientCriteriaKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "ClientCriteria")
public class ClientCriteria {

    @EmbeddedId
    private ClientCriteriaKey clientCriteriaKey;

    public ClientCriteria(ClientCriteriaKey clientCriteriaKey){
        this.clientCriteriaKey = clientCriteriaKey;
    }

    public ClientCriteria(){

    }
}
