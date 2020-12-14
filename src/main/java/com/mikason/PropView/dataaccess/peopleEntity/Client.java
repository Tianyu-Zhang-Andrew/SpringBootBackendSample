package com.mikason.PropView.dataaccess.peopleEntity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mikason.PropView.dataaccess.commercialEntity.ClientCriteria;
import com.mikason.PropView.dataaccess.commercialEntity.Rent;
import com.mikason.PropView.dataaccess.commercialEntity.Sale;
import com.mikason.PropView.dataaccess.commercialEntity.Visit;
import com.mikason.PropView.dataaccess.estateEntity.Address;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "Client")
public class Client {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    @JsonIgnoreProperties(value = "client", allowSetters=true)
    private Address address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    @JsonIgnoreProperties(value = "client", allowSetters=true)
    private Person person;

    @OneToMany(mappedBy="client", targetEntity = Visit.class)
    @JsonIgnoreProperties(value = "client", allowSetters = true)
    private Set<Visit> visits;

    @OneToMany(mappedBy="saleKey.client")
    @JsonIgnoreProperties(value = "saleKey.client", allowSetters = true)
    private Set<Sale> sales;

    @OneToMany(mappedBy="rentKey.client")
    @JsonIgnoreProperties(value = "rentKey.client", allowSetters=true)
    private Set<Rent> rents;

    @OneToMany(mappedBy="clientCriteriaKey.client", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = "clientCriteriaKey.client", allowSetters = true)
    private Set<ClientCriteria> clientCriteria;

    public Client(Address address, Person person){
        this.address = address;
        this.person = person;
    }

    public Client() {

    }

    public void addVisit(Visit visit){
        this.visits.add(visit);
    }

    public void deleteVisit(Visit visit){
        this.visits.remove(visit);
    }

    public void addSale(Sale sale){
        this.sales.add(sale);
    }

    public void deleteSale(Sale sale){
        this.sales.remove(sale);
    }

    public void addRent(Rent rent){
        this.rents.add(rent);
    }

    public void deleteRent(Rent rent){
        this.rents.remove(rent);
    }
}
