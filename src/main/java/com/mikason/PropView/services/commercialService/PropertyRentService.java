package com.mikason.PropView.services.commercialService;

import com.mikason.PropView.dataaccess.commercialEntity.PropertyRent;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PropertyRentService {
    ResponseEntity<PropertyRent> savePropertyRent(PropertyRent propertyRent);
    ResponseEntity<PropertyRent> editPropertyRent(PropertyRent propertyRent);
    ResponseEntity<List<PropertyRent>> searchPropertyRent(PropertyRent propertyRent, Long lowAvailablePriceLimit, Long highAvailablePriceLimit);
    ResponseEntity<List<PropertyRent>> getAllPropertyRent();
    ResponseEntity<PropertyRent> deleteAllPropertyRent();
    ResponseEntity<PropertyRent> deletePropertyRent(PropertyRent propertySale);
}
