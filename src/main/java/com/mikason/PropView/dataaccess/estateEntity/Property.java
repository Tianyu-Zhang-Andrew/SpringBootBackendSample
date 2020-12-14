package com.mikason.PropView.dataaccess.estateEntity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mikason.PropView.dataaccess.commercialEntity.*;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "Property")
public class Property {
    @Id
    @GeneratedValue
    private Long id;

    private Integer numRooms = null;
    private Integer numBathrooms = null;
    private Integer numGarages = null;
    private String description = null;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    @JsonIgnoreProperties(value = "property", allowSetters=true)
    private Address address;

    @OneToMany(mappedBy="ownerPropertyKey.property", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = "ownerPropertyKey.property", allowSetters = true)
    private Set<OwnerProperty> ownerProperties;

    @OneToMany(mappedBy="property", cascade = CascadeType.ALL, targetEntity = Visit.class)
    @JsonIgnoreProperties(value = "property", allowSetters=true)
    private Set<Visit> visits;

    @OneToMany(mappedBy="property", cascade = CascadeType.ALL, targetEntity = PropertySale.class)
    @JsonIgnoreProperties(value = "property", allowSetters=true)
    private Set<PropertySale> propertySales;

    @OneToMany(mappedBy="property", cascade = CascadeType.ALL, targetEntity = PropertyRent.class)
    @JsonIgnoreProperties(value = "property", allowSetters=true)
    private Set<PropertyRent> propertyRents;

    @OneToMany(mappedBy="propertyFeatureKey.property", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = "propertyFeatureKey.property", allowSetters=true)
    private Set<PropertyFeature> propertyFeatures;

    public Property(Integer numRooms, Integer numBathrooms, Integer numGarages, String description, Address address){
        this.numRooms = numRooms;
        this.numBathrooms = numBathrooms;
        this.numGarages = numGarages;
        this.description = description;
        this.address = address;
    }

    public Property(){

    }

    public void addVisit(Visit visit){
        this.visits.add(visit);
    }

    public void deleteVisit(Visit visit){
        this.visits.remove(visit);
    }

    public void addPropertySale(PropertySale propertySale){
        this.propertySales.add(propertySale);
    }

    public void deletePropertySale(PropertySale propertySale){
        this.propertySales.remove(propertySale);
    }

    public void addPropertyRent(PropertyRent propertyRent){
        this.propertyRents.add(propertyRent);
    }

    public void deletePropertyRent(PropertyRent propertyRent){
        this.propertyRents.remove(propertyRent);
    }

    public void deletePropertyFeature(PropertyFeature propertyFeature){
        this.propertyFeatures.remove(propertyFeature);
    }
}
