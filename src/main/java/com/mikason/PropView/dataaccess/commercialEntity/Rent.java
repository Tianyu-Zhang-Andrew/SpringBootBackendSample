package com.mikason.PropView.dataaccess.commercialEntity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mikason.PropView.dataaccess.compositeKey.RentKey;
import com.mikason.PropView.dataaccess.documentEntity.RentalAgreement;
import com.mikason.PropView.dataaccess.peopleEntity.Client;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "Rent")
public class Rent {

    @EmbeddedId
    private RentKey rentKey;

    @OneToMany(mappedBy="rent", cascade = {CascadeType.REFRESH, CascadeType.REMOVE, CascadeType.MERGE})
    @JsonIgnoreProperties(value = "rent", allowSetters=true)
    private Set<RentalAgreement> rentalAgreements;

    private Long rentPrice;
    private String rentEndDate;

    public Rent(String startDate, Agent agent, Client client, PropertyRent propertyRent, long rentPrice, String endDate){
        this.rentKey = new RentKey(startDate, agent, client, propertyRent);
        this.rentPrice = rentPrice;
        this.rentEndDate = endDate;
    }

    public Rent(){

    }

    public void addRentalAgreement(RentalAgreement rentalAgreement){
        this.rentalAgreements.add(rentalAgreement);
    }

    public void deleteRentalAgreement(RentalAgreement rentalAgreement){
        this.rentalAgreements.remove(rentalAgreement);
    }

}
