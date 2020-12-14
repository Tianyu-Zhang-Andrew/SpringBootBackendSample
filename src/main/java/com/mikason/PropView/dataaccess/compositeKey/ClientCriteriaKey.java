package com.mikason.PropView.dataaccess.compositeKey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mikason.PropView.dataaccess.commercialEntity.Feature;
import com.mikason.PropView.dataaccess.peopleEntity.Client;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
@Setter
@Getter
@Embeddable
public class ClientCriteriaKey implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "clientCriteria", allowSetters=true)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "clientCriteria", allowSetters=true)
    private Feature feature;

    public ClientCriteriaKey(Client client, Feature feature){
        this.client = client;
        this.feature = feature;
    }

    public ClientCriteriaKey(){

    }
}
