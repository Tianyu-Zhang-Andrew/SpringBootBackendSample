package com.mikason.PropView.dataaccess.repository;

import com.mikason.PropView.dataaccess.commercialEntity.Feature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeatureRepository extends JpaRepository<Feature, Long>{
}
