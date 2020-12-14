package com.mikason.PropView.controller;

import com.mikason.PropView.dataaccess.estateEntity.Property;
import com.mikason.PropView.services.estateService.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @PostMapping("/property/saveProperty")
    ResponseEntity<Property> saveProperty(@RequestBody Property property){
        return propertyService.saveProperty(property);
    }

    @PostMapping("/property/editProperty")
    ResponseEntity<Property> editProperty(@RequestBody Property property){
        return propertyService.editProperty(property);
    }

    @PostMapping("/property/deleteProperty")
    ResponseEntity<Property> deleteProperty(@RequestBody Property property){
        return propertyService.deleteProperty(property);
    }

    @PostMapping("/property/deleteAllProperty")
    ResponseEntity<Property> deleteAllProperty(){
        return propertyService.deleteAllProperty();
    }

    @PostMapping("/property/searchProperty")
    ResponseEntity<List<Property>> searchProperty(@RequestBody Property property){
        return propertyService.searchProperty(property);
    }

    @GetMapping("/property/getAllProperty")
    ResponseEntity<List<Property>> getAllProperty(){
        return propertyService.getAllProperty();
    }
}
