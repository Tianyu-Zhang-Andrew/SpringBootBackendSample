package com.mikason.PropView.controller;

import com.mikason.PropView.dataaccess.commercialEntity.Feature;
import com.mikason.PropView.services.commercialService.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FeatureController {

    @Autowired
    private FeatureService featureService;

    @PostMapping("/feature/saveFeature")
    ResponseEntity<Feature> saveFeature(@RequestBody Feature feature){
        return featureService.saveFeature(feature);
    }

    @PostMapping("/feature/editFeature")
    ResponseEntity<Feature> editFeature(@RequestBody Feature feature){
        return featureService.editFeature(feature);
    }

    @PostMapping("/feature/deleteFeature")
    ResponseEntity<Feature> deleteFeature(@RequestBody Feature feature){
        return featureService.deleteFeature(feature);
    }

    @PostMapping("/feature/deleteAllFeature")
    ResponseEntity<Feature> deleteAllFeature(){
        return featureService.deleteAllFeature();
    }

    @PostMapping("/feature/searchFeature")
    ResponseEntity<List<Feature>> searchFeature(@RequestBody Feature feature){
        return featureService.searchFeature(feature);
    }

    @GetMapping("/feature/getAllFeature")
    ResponseEntity<List<Feature>> getAllFeature(){
        return featureService.getAllFeature();
    }
}
