package com.mikason.PropView.dataaccess.repository;

import com.mikason.PropView.dataaccess.commercialEntity.Sale;
import com.mikason.PropView.dataaccess.compositeKey.SaleKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, SaleKey> {
}
