package com.mikason.PropView.dataaccess.repository;

import com.mikason.PropView.dataaccess.commercialEntity.PropertyFeature;
import com.mikason.PropView.dataaccess.compositeKey.PropertyFeatureKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyFeatureRepository extends JpaRepository<PropertyFeature, PropertyFeatureKey> {
}
