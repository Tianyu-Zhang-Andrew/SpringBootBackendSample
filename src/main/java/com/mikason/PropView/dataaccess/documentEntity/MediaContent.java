package com.mikason.PropView.dataaccess.documentEntity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mikason.PropView.dataaccess.commercialEntity.PropertyRent;
import com.mikason.PropView.dataaccess.commercialEntity.PropertySale;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "MediaContent")
public class MediaContent {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propertySale_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "mediaContents", allowSetters=true)
    private PropertySale propertySale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propertyRent_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "mediaContents", allowSetters=true)
    private PropertyRent propertyRent;

    private String type;
    private String content;

    public MediaContent(String type, String content, PropertySale propertySale, PropertyRent propertyRent){
        this.type = type;
        this.content = content;
        this.propertySale = propertySale;
        this.propertyRent = propertyRent;
    }

    public MediaContent(){

    }
}
