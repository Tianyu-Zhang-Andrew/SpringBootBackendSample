package com.mikason.PropView.dataaccess.estateEntity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mikason.PropView.dataaccess.peopleEntity.Client;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

@Data
@Entity
@Getter
@Table(name = "Address")
public class Address {
    @Id
    @GeneratedValue
    private Long id;

    private String unitNumber = null;
    private String streetNumber = null;
    private String streetName = null;
    private String streetType = null;
    private String suburb = null;
    private String state = null;
    private String country = null;
    private String postCode = null;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "address")
    @JsonIgnoreProperties(value = "address", allowSetters=true)
    private Property property;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "address")
    @JsonIgnoreProperties(value = "address", allowSetters=true)
    private Client client;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "address")
    @JsonIgnoreProperties(value = "address", allowSetters=true)
    private Office office;

    public Address(String unitNumber, String streetNumber, String streetName, String streetType, String suburb, String state, String country, String postCode) {
        this.unitNumber = unitNumber;
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.streetType = streetType;
        this.suburb = suburb;
        this.state = state;
        this.country = country;
        this.postCode = postCode;
    }

    public Address(){

    }
}
