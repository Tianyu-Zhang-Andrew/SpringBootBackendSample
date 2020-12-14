package com.mikason.PropView.services.commercialService;

import com.mikason.PropView.Exception.commercialEntityException.FeatureAlreadyExistException;
import com.mikason.PropView.Exception.commercialEntityException.FeatureNotFoundException;
import com.mikason.PropView.Exception.databaseException.DatabaseIsEmptyException;
import com.mikason.PropView.Exception.databaseException.NoResultFoundException;
import com.mikason.PropView.dataaccess.commercialEntity.Feature;
import com.mikason.PropView.dataaccess.repository.FeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeatureServiceImp implements FeatureService{

    @Autowired
    private FeatureRepository featureRepository;

    @Override
    public ResponseEntity<Feature> saveFeature(Feature feature) {
        Example<Feature> featureExample =  Example.of(feature);
        Optional<Feature> existFeature = this.featureRepository.findOne(featureExample);

        if(existFeature.isPresent()) {
            throw new FeatureAlreadyExistException();
        }else {
            Feature savedFeature = this.featureRepository.save(feature);
            return new ResponseEntity<>(savedFeature, HttpStatus.CREATED);
        }
    }

    @Override
    public ResponseEntity<Feature> editFeature(Feature feature) {
        Optional<Feature> existFeature = this.featureRepository.findById(feature.getId());

        if(!existFeature.isPresent()) {
            throw new FeatureNotFoundException(feature.getId());
        }else {
            Feature savedFeature = this.featureRepository.save(feature);
            return new ResponseEntity<>(savedFeature, HttpStatus.CREATED);
        }
    }

    @Override
    public ResponseEntity<Feature> deleteFeature(Feature feature) {
        Optional<Feature> existFeature= this.featureRepository.findById(feature.getId());

        if(!existFeature.isPresent()) {
            throw new FeatureNotFoundException(feature.getId());
        }else {
            this.featureRepository.delete(feature);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @Override
    public ResponseEntity<Feature> deleteAllFeature() {
        this.featureRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<Feature>> searchFeature(Feature feature) {
        Example<Feature> featureExample =  Example.of(feature);
        List<Feature> featureList = this.featureRepository.findAll(featureExample);

        if(featureList.size() == 0){
            throw new NoResultFoundException();
        }else {
            return new ResponseEntity<>(featureList, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<List<Feature>> getAllFeature() {
        List<Feature> featureList = this.featureRepository.findAll();

        if(featureList.size() == 0){
            throw new DatabaseIsEmptyException();
        }else{
            return new ResponseEntity<>(featureList, HttpStatus.OK);
        }
    }
}
