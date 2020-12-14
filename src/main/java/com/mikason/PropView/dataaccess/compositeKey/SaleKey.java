package com.mikason.PropView.dataaccess.compositeKey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mikason.PropView.dataaccess.commercialEntity.Agent;
import com.mikason.PropView.dataaccess.commercialEntity.PropertySale;
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
public class SaleKey implements Serializable {
    private String saleDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "sales", allowSetters=true)
    private Agent agent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "sales", allowSetters=true)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propertySale_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "sales", allowSetters=true)
    private PropertySale propertySale;

    public SaleKey( String date, Agent agent, Client client, PropertySale propertySale){
        this.saleDate = date;
        this.agent = agent;
        this.client = client;
        this.propertySale = propertySale;
    }

    public SaleKey(){}
}
