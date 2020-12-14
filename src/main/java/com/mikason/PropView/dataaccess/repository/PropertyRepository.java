package com.mikason.PropView.dataaccess.repository;

import com.mikason.PropView.dataaccess.estateEntity.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property, Long> {
}
