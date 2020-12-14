package com.mikason.PropView.dataaccess.documentEntity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mikason.PropView.dataaccess.commercialEntity.Sale;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "SaleDocuments")
public class SaleDocument {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = "saleDocuments", allowSetters=true)
    private Sale sale;

    private String content;

    public  SaleDocument(Sale sale, String content){
        this.sale = sale;
        this.content = content;
    }

    public SaleDocument(){

    }
}
