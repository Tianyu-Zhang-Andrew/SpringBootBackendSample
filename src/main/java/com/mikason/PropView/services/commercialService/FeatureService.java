package com.mikason.PropView.services.commercialService;

import com.mikason.PropView.dataaccess.commercialEntity.Feature;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FeatureService {
    ResponseEntity<Feature> saveFeature(Feature feature);
    ResponseEntity<Feature> editFeature(Feature feature);
    ResponseEntity<Feature> deleteFeature(Feature feature);
    ResponseEntity<Feature> deleteAllFeature();
    ResponseEntity<List<Feature>> searchFeature(Feature feature);
    ResponseEntity<List<Feature>> getAllFeature();
}
