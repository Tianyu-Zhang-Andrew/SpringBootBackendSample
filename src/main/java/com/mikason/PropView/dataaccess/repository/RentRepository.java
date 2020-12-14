package com.mikason.PropView.dataaccess.repository;

import com.mikason.PropView.dataaccess.commercialEntity.Rent;
import com.mikason.PropView.dataaccess.compositeKey.RentKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentRepository extends JpaRepository<Rent, RentKey> {
}
