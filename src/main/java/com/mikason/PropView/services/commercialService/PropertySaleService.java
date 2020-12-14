package com.mikason.PropView.services.commercialService;

import com.mikason.PropView.dataaccess.commercialEntity.PropertySale;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PropertySaleService {
    ResponseEntity<PropertySale> savePropertySale(PropertySale propertySale);
    ResponseEntity<PropertySale> editPropertySale(PropertySale propertySale);
    ResponseEntity<PropertySale> deletePropertySale(PropertySale propertySale);
    ResponseEntity<PropertySale> deleteAllPropertySale();
    ResponseEntity<List<PropertySale>> searchPropertySale(PropertySale propertySale, Long lowSuggestedPriceLimit, Long highSuggestedPriceLimit);
    ResponseEntity<List<PropertySale>> getAllPropertySale();
}
