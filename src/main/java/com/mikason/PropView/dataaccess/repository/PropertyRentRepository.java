package com.mikason.PropView.dataaccess.repository;

import com.mikason.PropView.dataaccess.commercialEntity.PropertyRent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRentRepository extends JpaRepository<PropertyRent, Long> {
}
