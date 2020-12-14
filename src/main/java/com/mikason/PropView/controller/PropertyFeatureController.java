package com.mikason.PropView.controller;

import com.google.gson.Gson;
import com.mikason.PropView.dataaccess.commercialEntity.PropertyFeature;
import com.mikason.PropView.services.commercialService.PropertyFeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PropertyFeatureController {
    @Autowired
    private PropertyFeatureService propertyFeatureService;

    @PostMapping("/propertyFeature/savePropertyFeature")
    ResponseEntity<PropertyFeature> savePropertyFeature(@RequestBody PropertyFeature propertyFeature){
        return propertyFeatureService.savePropertyFeature(propertyFeature);
    }

    @PostMapping("/propertyFeature/editPropertyFeature")
    ResponseEntity<PropertyFeature> editPropertyFeature(@RequestParam(value = "oldPropertyFeature", required=false) String oldPropertyFeatureStr,
                                  @RequestParam(value = "newPropertyFeature",required=false) String newPropertyFeatureStr){

        Gson gson = new Gson();
        PropertyFeature oldPropertyFeature = gson.fromJson(oldPropertyFeatureStr, PropertyFeature.class);
        PropertyFeature newPropertyFeature = gson.fromJson(newPropertyFeatureStr, PropertyFeature.class);

        return propertyFeatureService.editPropertyFeature(oldPropertyFeature, newPropertyFeature);
    }

    @PostMapping("/propertyFeature/deletePropertyFeature")
    ResponseEntity<PropertyFeature> deletePropertyFeature(@RequestBody PropertyFeature propertyFeature){
        return propertyFeatureService.deletePropertyFeature(propertyFeature);
    }

    @PostMapping("/propertyFeature/deleteAllPropertyFeature")
    ResponseEntity<PropertyFeature> deleteAllPropertyFeature(){
        return propertyFeatureService.deleteAllPropertyFeature();
    }

    @PostMapping("/propertyFeature/searchPropertyFeature")
    ResponseEntity<List<PropertyFeature>> searchPropertyFeature(@RequestBody PropertyFeature propertyFeature){
        return propertyFeatureService.searchPropertyFeature(propertyFeature);
    }

    @GetMapping("/propertyFeature/getAllPropertyFeature")
    ResponseEntity<List<PropertyFeature>> getAllPropertyFeature(){
        return propertyFeatureService.getAllPropertyFeature();
    }
}
