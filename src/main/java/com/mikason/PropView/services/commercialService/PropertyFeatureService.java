package com.mikason.PropView.services.commercialService;

import com.mikason.PropView.dataaccess.commercialEntity.PropertyFeature;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PropertyFeatureService {
    ResponseEntity<PropertyFeature> savePropertyFeature(PropertyFeature propertyFeature);
    ResponseEntity<PropertyFeature> editPropertyFeature(PropertyFeature oldPropertyFeature, PropertyFeature newPropertyFeature);
    ResponseEntity<PropertyFeature> deletePropertyFeature(PropertyFeature propertyFeature);
    ResponseEntity<PropertyFeature> deleteAllPropertyFeature();
    ResponseEntity<List<PropertyFeature>> searchPropertyFeature(PropertyFeature propertyFeature);
    ResponseEntity<List<PropertyFeature>> getAllPropertyFeature();
}
