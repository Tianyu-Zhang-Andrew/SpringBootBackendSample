package com.mikason.PropView.services.commercialService;

import com.mikason.PropView.dataaccess.commercialEntity.OwnerProperty;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OwnerPropertyService {
    ResponseEntity<OwnerProperty> saveOwnerProperty(OwnerProperty ownerProperty);
    ResponseEntity<OwnerProperty> editOwnerProperty(OwnerProperty oldOwnerProperty, OwnerProperty newOwnerProperty);
    ResponseEntity<OwnerProperty> deleteOwnerProperty(OwnerProperty ownerProperty);
    ResponseEntity<OwnerProperty> deleteAllOwnerProperty();
    ResponseEntity<List<OwnerProperty>> searchOwnerProperty(OwnerProperty ownerProperty);
    ResponseEntity<List<OwnerProperty>> getAllOwnerProperty();
}
