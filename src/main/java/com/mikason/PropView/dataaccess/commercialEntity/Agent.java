package com.mikason.PropView.dataaccess.commercialEntity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mikason.PropView.dataaccess.estateEntity.Office;
import com.mikason.PropView.dataaccess.peopleEntity.Person;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "Agent")
public class Agent {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    @JsonIgnoreProperties(value = "agent", allowSetters=true)
    private Person person = null;

    @OneToMany(mappedBy="agent", targetEntity = Visit.class)
    @JsonIgnoreProperties(value = "agent", allowSetters=true)
    private Set<Visit> visits;

    @OneToMany(mappedBy="saleKey.agent")
    @JsonIgnoreProperties(value = "saleKey.agent", allowSetters=true)
    private Set<Sale> sales;

    @OneToMany(mappedBy="rentKey.agent")
    @JsonIgnoreProperties(value = "rentKey.agent", allowSetters=true)
    private Set<Rent> rents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "agents", allowSetters=true)
    private Office office;

    public Agent(Person person, Office office){
        this.person = person;
        this.office = office;
    }

    public Agent(){

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