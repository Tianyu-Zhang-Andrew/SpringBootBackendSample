package com.mikason.PropView.dataaccess.peopleEntity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mikason.PropView.dataaccess.commercialEntity.OwnerProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "Owner")
public class Owner {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    @JsonIgnoreProperties(value = "owner", allowSetters = true)
    private Person person;

    @OneToMany(mappedBy="ownerPropertyKey.owner", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = "ownerPropertyKey.owner", allowSetters = true)
    private Set<OwnerProperty> ownerProperties;

    private String dateOfPurchase;

    public Owner(String dateOfPurchase, Person person){
        this.dateOfPurchase = dateOfPurchase;
        this.person = person;
    }

    public Owner(){

    }
}
