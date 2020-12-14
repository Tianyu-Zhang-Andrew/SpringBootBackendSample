package com.mikason.PropView.dataaccess.repository;


import com.mikason.PropView.dataaccess.peopleEntity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
