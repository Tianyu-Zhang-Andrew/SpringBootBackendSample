package com.mikason.PropView.dataaccess.estateEntity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mikason.PropView.dataaccess.commercialEntity.Agent;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "Office")
public class Office {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String phoneNumber;
    private String faxNumber;
    private String emailAddress;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    @JsonIgnoreProperties(value = "office", allowSetters=true)
    private Address address;

    @OneToMany(mappedBy="office", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = "office", allowSetters=true)
//    private Set<Agent> agents;
    private List<Agent> agents = new ArrayList<>();

    public Office(String name, String phoneNumber, String faxNumber, String emailAddress, Address address){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.faxNumber = faxNumber;
        this.emailAddress = emailAddress;
        this.address = address;
    }

    public Office(){}

}
