package com.mikason.PropView.dataaccess.commercialEntity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.Gson;
import com.mikason.PropView.Exception.commercialEntityException.VisitTimeException;
import com.mikason.PropView.dataaccess.estateEntity.Property;
import com.mikason.PropView.dataaccess.peopleEntity.Client;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

@Data
@Getter
@Entity
@Table(name = "Visit")
public class Visit {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "visits", allowSetters=true)
    private Agent agent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "visits", allowSetters=true)
    private Property property;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "visits", allowSetters=true)
    private Client client;

    private String duration;

    public Visit(Agent agent, Property property, Client client, String duration){

        Gson gson = new Gson();
        Duration duration1 = gson.fromJson(duration, Duration.class);
        if(duration1.getStartTime().after(duration1.getEndTime())){
            throw new VisitTimeException();
        }

        this.agent = agent;
        this.property = property;
        this.client = client;
        this.duration = duration;
    }

    public Visit(){

    }

}
