package com.mikason.PropView.controller;

import com.google.gson.Gson;
import com.mikason.PropView.dataaccess.commercialEntity.OwnerProperty;
import com.mikason.PropView.services.commercialService.OwnerPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OwnerPropertyController {
    @Autowired
    private OwnerPropertyService ownerPropertyService;

    @PostMapping("/ownerProperty/saveOwnerProperty")
    ResponseEntity<OwnerProperty> saveOwnerProperty(@RequestBody OwnerProperty ownerProperty){
        return ownerPropertyService.saveOwnerProperty(ownerProperty);
    }

    @PostMapping("/ownerProperty/editOwnerProperty")
    ResponseEntity<OwnerProperty> editOwnerProperty(@RequestParam(value = "oldOwnerProperty", required=false) String oldOwnerPropertyStr,
                                  @RequestParam(value = "newOwnerProperty",required=false) String newOwnerPropertyStr){

        Gson gson = new Gson();
        OwnerProperty oldOwnerProperty = gson.fromJson(oldOwnerPropertyStr, OwnerProperty.class);
        OwnerProperty newSOwnerProperty = gson.fromJson(newOwnerPropertyStr, OwnerProperty.class);

        return ownerPropertyService.editOwnerProperty(oldOwnerProperty, newSOwnerProperty);
    }

    @PostMapping("/ownerProperty/deleteOwnerProperty")
    ResponseEntity<OwnerProperty> deleteOwnerProperty(@RequestBody OwnerProperty ownerProperty){
        return ownerPropertyService.deleteOwnerProperty(ownerProperty);
    }

    @PostMapping("/ownerProperty/deleteAllOwnerProperty")
    ResponseEntity<OwnerProperty> deleteAllOwnerProperty(){
        return ownerPropertyService.deleteAllOwnerProperty();
    }

    @PostMapping("/ownerProperty/searchOwnerProperty")
    ResponseEntity<List<OwnerProperty>> searchOwnerProperty(@RequestBody OwnerProperty ownerProperty){
        return ownerPropertyService.searchOwnerProperty(ownerProperty);
    }

    @GetMapping("/ownerProperty/getAllOwnerProperty")
    ResponseEntity<List<OwnerProperty>> getAllOwnerProperty(){
        return ownerPropertyService.getAllOwnerProperty();
    }
}
