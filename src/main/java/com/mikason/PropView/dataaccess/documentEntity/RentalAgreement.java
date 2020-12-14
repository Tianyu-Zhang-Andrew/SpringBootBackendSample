package com.mikason.PropView.dataaccess.documentEntity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mikason.PropView.dataaccess.commercialEntity.Rent;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "RentalAgreement")
public class RentalAgreement {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumns({
//            @JoinColumn(name="agent_id", referencedColumnName="agent_id"),
//            @JoinColumn(name="client_id", referencedColumnName="client_id"),
//            @JoinColumn(name="propertyRent_id", referencedColumnName="propertyRent_id")
//    })
    @JsonIgnoreProperties(value = "rentalAgreements", allowSetters=true)
    private Rent rent;

    private String content;

    public  RentalAgreement(Rent rent, String content){
        this.rent = rent;
        this.content = content;
    }

    public RentalAgreement(){

    }
}
