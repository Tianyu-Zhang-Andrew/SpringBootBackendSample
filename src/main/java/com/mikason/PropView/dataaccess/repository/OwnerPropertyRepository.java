package com.mikason.PropView.dataaccess.repository;

import com.mikason.PropView.dataaccess.commercialEntity.OwnerProperty;
import com.mikason.PropView.dataaccess.compositeKey.OwnerPropertyKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerPropertyRepository extends JpaRepository<OwnerProperty, OwnerPropertyKey>{
}
