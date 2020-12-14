package com.mikason.PropView.controller;

import com.google.gson.Gson;
import com.mikason.PropView.dataaccess.commercialEntity.PropertySale;
import com.mikason.PropView.services.commercialService.PropertySaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PropertySaleController {

    @Autowired
    private PropertySaleService propertySaleService;

    @PostMapping("/propertySale/savePropertySale")
    ResponseEntity<PropertySale> savePropertySale(@RequestBody PropertySale propertySale){
        return propertySaleService.savePropertySale(propertySale);
    }

    @PostMapping("/propertySale/editPropertySale")
    ResponseEntity<PropertySale> editPropertySale(@RequestBody PropertySale propertySale){
        return propertySaleService.editPropertySale(propertySale);
    }

    @PostMapping("/propertySale/deletePropertySale")
    ResponseEntity<PropertySale> deletepropertySale(@RequestBody PropertySale propertySale){
        return propertySaleService.deletePropertySale(propertySale);
    }

    @PostMapping("/propertySale/deleteAllPropertySale")
    ResponseEntity<PropertySale> deleteAllpropertySale(){
        return propertySaleService.deleteAllPropertySale();
    }

    @PostMapping("/propertySale/searchPropertySale")
    ResponseEntity<List<PropertySale>> searchPropertySale(@RequestParam(value = "propertySale", required=false) String propertySale,
                                                   @RequestParam(value = "lowPriceLimit",required=false) String lowSuggestedPriceLimit,
                                                   @RequestParam(value = "highPriceLimit", required=false) String highSuggestedPriceLimit){
        Gson gson = new Gson();
        PropertySale receivedPropertySaleObj = gson.fromJson(propertySale, PropertySale.class);

        Long low = null;
        Long high = null;

        if(lowSuggestedPriceLimit != null){
            low = Long.valueOf(lowSuggestedPriceLimit);
        }

        if(highSuggestedPriceLimit != null){
            high = Long.valueOf(highSuggestedPriceLimit);
        }

        return propertySaleService.searchPropertySale(receivedPropertySaleObj, low, high);
    }

    @GetMapping("/propertySale/getAllPropertySale")
    ResponseEntity<List<PropertySale>> getAllpropertySale(){
        return propertySaleService.getAllPropertySale();
    }
}
