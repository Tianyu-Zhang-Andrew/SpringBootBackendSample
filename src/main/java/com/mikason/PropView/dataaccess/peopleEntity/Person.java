package com.mikason.PropView.dataaccess.peopleEntity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mikason.PropView.dataaccess.commercialEntity.Agent;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

@Data
@Getter
@Entity
//@Table(name = "Person", uniqueConstraints={@UniqueConstraint(columnNames ={"title","firstName", "middleName", "lastName", "gender", "birthDate", "mobileNumber", "email"})})
public class Person {
    @Id
    @GeneratedValue
    private Long id;

    private Title title = null;
    private String firstName = null;
    private String middleName = null;
    private String lastName = null;
    private Gender gender = null;
    private String birthDate = null;
    private String mobileNumber = null;
    private String email = null;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "person")
    @JsonIgnoreProperties(value = "person", allowSetters=true)
    private Client client = null;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "person")
    @JsonIgnoreProperties(value = "person", allowSetters=true)
    private Agent agent = null;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "person")
    @JsonIgnoreProperties(value = "person", allowSetters=true)
    private Owner owner = null;

    public Person(Title title, String firstName, String middleName,String lastName, Gender gender, String birthDate, String mobileNumber, String email){
        this.title = title;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.mobileNumber = mobileNumber;
        this.email = email;
    }

    public Person(){

    }

}
