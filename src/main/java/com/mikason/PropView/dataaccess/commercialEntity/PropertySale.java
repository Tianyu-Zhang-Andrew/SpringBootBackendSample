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
@Table(name = "PropertySale")
public class PropertySale {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "propertySales", allowSetters=true)
    private Property property;

    @OneToMany(mappedBy="saleKey.propertySale", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = "saleKey.propertySale", allowSetters=true)
    private Set<Sale> sales;

    private Long suggestedSalesPrice;

    @OneToMany(mappedBy="propertySale", cascade = {CascadeType.REFRESH, CascadeType.REMOVE})
    @JsonIgnoreProperties(value = "propertySale", allowSetters=true)
    private Set<MediaContent> mediaContents;

    public PropertySale(Property property, long suggestedSalesPrice){
        this.property = property;
        this.suggestedSalesPrice = suggestedSalesPrice;
    }

    public PropertySale(){}

    public void addSale(Sale sale){
        this.sales.add(sale);
    }

    public void deleteSale(Sale sale){
        this.sales.remove(sale);
    }

    public void setSale(Set<Sale> sale){
        this.sales.clear();
        this.sales.addAll(sale);
    }

    public void addMediaContent(MediaContent content){
        this.mediaContents.add(content);
    }

    public void deleteMediaContent(MediaContent content){
        this.mediaContents.remove(content);
    }

}
