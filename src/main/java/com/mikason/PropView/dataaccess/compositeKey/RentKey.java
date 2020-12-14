package com.mikason.PropView.dataaccess.compositeKey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mikason.PropView.dataaccess.commercialEntity.Agent;
import com.mikason.PropView.dataaccess.commercialEntity.PropertyRent;
import com.mikason.PropView.dataaccess.peopleEntity.Client;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Setter
@Getter
@Embeddable
public class RentKey implements Serializable{
    private String rentStartDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "rents", allowSetters=true)
    private Agent agent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "rents", allowSetters=true)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propertyRent_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "rents", allowSetters=true)
    private PropertyRent propertyRent;


    public RentKey(String startDate, Agent agent, Client client, PropertyRent propertyRent){
        this.rentStartDate = startDate;
        this.agent = agent;
        this.client = client;
        this.propertyRent = propertyRent;
    }

    public RentKey(){}
}
