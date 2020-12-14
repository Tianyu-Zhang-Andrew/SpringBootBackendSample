package com.mikason.PropView.dataaccess.commercialEntity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "Feature")
public class Feature {
    @Id
    @GeneratedValue
    private Long id;

    private String code;
    private String description;

    @OneToMany(mappedBy="propertyFeatureKey.feature", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = "propertyFeatureKey.feature", allowSetters=true)
    private Set<PropertyFeature> propertyFeatures;

    @OneToMany(mappedBy="clientCriteriaKey.feature", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = "clientCriteriaKey.feature", allowSetters = true)
    private Set<ClientCriteria> clientCriteria;

    public Feature(String code, String description){
        this.code = code;
        this.description = description;
    }

    public Feature(){

    }
}
