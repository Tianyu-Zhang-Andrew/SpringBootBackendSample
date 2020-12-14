package com.mikason.PropView.dataaccess.commercialEntity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mikason.PropView.dataaccess.documentEntity.MediaContent;
import com.mikason.PropView.dataaccess.estateEntity.Property;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "PropertyRent")
public class PropertyRent {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "propertyRents", allowSetters=true)
    private Property property;

    @OneToMany(mappedBy="rentKey.propertyRent", cascade = {CascadeType.REFRESH, CascadeType.REMOVE, CascadeType.MERGE})
    @JsonIgnoreProperties(value = "rentKey.propertyRent", allowSetters=true)
    private Set<Rent> rents;

    @OneToMany(mappedBy="propertyRent", cascade = {CascadeType.REFRESH, CascadeType.REMOVE, CascadeType.MERGE})
    @JsonIgnoreProperties(value = "propertyRent", allowSetters=true)
    private Set<MediaContent> mediaContents;

    private Long suggestedRentalPrice;
    private String availableRentalStartDate;
    private String availableRentalEndDate;

    public PropertyRent(Property property, long suggestedPrice, String startDate, String endDate){
        this.property = property;
        this.suggestedRentalPrice = suggestedPrice;
        this.availableRentalStartDate = startDate;
        this.availableRentalEndDate = endDate;
    }

    public PropertyRent(){}

    public void addRent(Rent rent){
        this.rents.add(rent);
    }

    public void deleteRent(Rent rent){
        this.rents.remove(rent);
    }

    public void addMediaContent(MediaContent content){
        this.mediaContents.add(content);
    }

    public void deleteMediaContent(MediaContent content){
        this.mediaContents.remove(content);
    }
}
