package com.mikason.PropView.dataaccess.commercialEntity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mikason.PropView.dataaccess.compositeKey.SaleKey;
import com.mikason.PropView.dataaccess.documentEntity.SaleDocument;
import com.mikason.PropView.dataaccess.peopleEntity.Client;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "Sale")
public class Sale {

    @EmbeddedId
    private SaleKey saleKey;
    private Long salePrice;

    @OneToMany(mappedBy="sale", cascade = {CascadeType.REFRESH, CascadeType.REMOVE, CascadeType.MERGE})
    @JsonIgnoreProperties(value = "sale", allowSetters=true)
    private Set<SaleDocument> saleDocuments;

    public Sale(String saleDate, Agent agent, Client client, PropertySale propertySale, long salePrice){
        SaleKey key = new SaleKey(saleDate,agent,client,propertySale);
        this.saleKey = key;
        this.salePrice = salePrice;
    }

    public Sale(){}

    public void deleteSaleDocument(SaleDocument saleDocument){
        this.saleDocuments.remove(saleDocument);
    }

}
