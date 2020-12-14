package com.mikason.PropView.dataaccess.repository;

import com.mikason.PropView.dataaccess.commercialEntity.ClientCriteria;
import com.mikason.PropView.dataaccess.compositeKey.ClientCriteriaKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientCriteriaRepository extends JpaRepository<ClientCriteria, ClientCriteriaKey>  {
}
