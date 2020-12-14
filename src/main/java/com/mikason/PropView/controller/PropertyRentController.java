package com.mikason.PropView.controller;

import com.google.gson.Gson;
import com.mikason.PropView.dataaccess.commercialEntity.PropertyRent;
import com.mikason.PropView.services.commercialService.PropertyRentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PropertyRentController {
    @Autowired
    private PropertyRentService propertyRentService;

    @PostMapping("/propertyRent/savePropertyRent")
    ResponseEntity<PropertyRent> savePropertyRent(@RequestBody PropertyRent propertyRent){
        return propertyRentService.savePropertyRent(propertyRent);
    }

    @PostMapping("/propertyRent/editPropertyRent")
    ResponseEntity<PropertyRent> editPropertyRent(@RequestBody PropertyRent propertyRent){
        return propertyRentService.editPropertyRent(propertyRent);
    }

    @PostMapping("/propertyRent/searchPropertyRent")
    ResponseEntity<List<PropertyRent>> searchPropertyRent(@RequestParam(value = "propertyRent", required=false) String propertyRent,
                                                          @RequestParam(value = "lowPriceLimit",required=false) String lowAvailablePriceLimit,
                                                          @RequestParam(value = "highPriceLimit", required=false) String highAvailablePriceLimit){

        Gson gson = new Gson();
        PropertyRent receivedPropertyRentObj = gson.fromJson(propertyRent, PropertyRent.class);
        Long low = null;
        Long high = null;

        if(lowAvailablePriceLimit != null){
            low = Long.valueOf(lowAvailablePriceLimit);
        }

        if(highAvailablePriceLimit != null){
            high = Long.valueOf(highAvailablePriceLimit);
        }

        return propertyRentService.searchPropertyRent(receivedPropertyRentObj, low, high);
    }

    @PostMapping("/propertyRent/deletePropertyRent")
    ResponseEntity<PropertyRent> deletepropertyRent(@RequestBody PropertyRent propertySale){
        return propertyRentService.deletePropertyRent(propertySale);
    }

    @PostMapping("/propertyRent/deleteAllPropertyRent")
    ResponseEntity<PropertyRent> deleteAllpropertyRent(){
        return propertyRentService.deleteAllPropertyRent();
    }

    @GetMapping("/propertyRent/getAllPropertyRent")
    ResponseEntity<List<PropertyRent>> getAllPropertyRent(){
        return propertyRentService.getAllPropertyRent();
    }
}
