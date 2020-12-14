package com.mikason.PropView.services.estateService;

import com.mikason.PropView.dataaccess.estateEntity.Property;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PropertyService {
    ResponseEntity<Property> saveProperty(Property property);
    ResponseEntity<Property> editProperty(Property property);
    ResponseEntity<Property> deleteProperty(Property property);
    ResponseEntity<Property> deleteAllProperty();
    ResponseEntity<List<Property>> searchProperty(Property property);
    ResponseEntity<List<Property>> getAllProperty();
}
