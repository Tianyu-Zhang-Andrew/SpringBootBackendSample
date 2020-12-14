package com.mikason.PropView.dataaccess.repository;

import com.mikason.PropView.dataaccess.peopleEntity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long>{
}
