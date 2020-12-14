package com.mikason.PropView.dataaccess.compositeKey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mikason.PropView.dataaccess.estateEntity.Property;
import com.mikason.PropView.dataaccess.peopleEntity.Owner;
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
public class OwnerPropertyKey implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "ownerProperties", allowSetters=true)
    private Owner owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "ownerProperties", allowSetters=true)
    private Property property;

    public OwnerPropertyKey(Owner owner, Property property){
        this.owner = owner;
        this.property = property;
    }

    public OwnerPropertyKey(){

    }
}
