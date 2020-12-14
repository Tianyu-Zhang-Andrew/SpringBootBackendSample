package com.mikason.PropView.dataaccess.commercialEntity;

import com.mikason.PropView.dataaccess.compositeKey.PropertyFeatureKey;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Setter
@Getter
@Entity
@Table(name = "PropertyFeature")
public class PropertyFeature {
    @EmbeddedId
    private PropertyFeatureKey propertyFeatureKey;

    public PropertyFeature(){

    }

    public PropertyFeature(PropertyFeatureKey propertyFeatureKey){
        this.propertyFeatureKey = propertyFeatureKey;
    }
}
