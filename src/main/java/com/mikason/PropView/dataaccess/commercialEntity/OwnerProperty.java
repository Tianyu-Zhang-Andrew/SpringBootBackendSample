package com.mikason.PropView.dataaccess.commercialEntity;

import com.mikason.PropView.dataaccess.compositeKey.OwnerPropertyKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "OwnerProperty")
public class OwnerProperty {

    @EmbeddedId
    private OwnerPropertyKey ownerPropertyKey;

    public OwnerProperty(OwnerPropertyKey ownerPropertyKey){
        this.ownerPropertyKey = ownerPropertyKey;
    }

    public OwnerProperty(){

    }
}
