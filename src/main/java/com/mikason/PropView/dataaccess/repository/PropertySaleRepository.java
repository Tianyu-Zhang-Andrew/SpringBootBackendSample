package com.mikason.PropView.dataaccess.repository;

import com.mikason.PropView.dataaccess.commercialEntity.PropertySale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertySaleRepository extends JpaRepository<PropertySale, Long> {
}
