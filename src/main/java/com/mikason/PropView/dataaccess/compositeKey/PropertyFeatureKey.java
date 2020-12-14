package com.mikason.PropView.dataaccess.compositeKey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mikason.PropView.dataaccess.commercialEntity.Feature;
import com.mikason.PropView.dataaccess.estateEntity.Property;
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
public class PropertyFeatureKey implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "propertyFeatures", allowSetters=true)
    private Property property;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "propertyFeatures", allowSetters=true)
    private Feature feature;

    public PropertyFeatureKey(){}

    public PropertyFeatureKey(Property property, Feature feature){
        this.property = property;
        this.feature = feature;
    }
}
