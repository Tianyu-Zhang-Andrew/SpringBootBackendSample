package com.mikason.PropView.services.commercialService;

import com.mikason.PropView.Exception.commercialEntityException.FeatureNotFoundException;
import com.mikason.PropView.Exception.commercialEntityException.PropertyFeatureAlreadyExistException;
import com.mikason.PropView.Exception.commercialEntityException.PropertyFeatureNotFoundException;
import com.mikason.PropView.Exception.databaseException.DatabaseIsEmptyException;
import com.mikason.PropView.Exception.estateEntityException.PropertyNotFoundException;
import com.mikason.PropView.dataaccess.commercialEntity.Feature;
import com.mikason.PropView.dataaccess.commercialEntity.PropertyFeature;
import com.mikason.PropView.dataaccess.estateEntity.Property;
import com.mikason.PropView.dataaccess.repository.FeatureRepository;
import com.mikason.PropView.dataaccess.repository.PropertyFeatureRepository;
import com.mikason.PropView.dataaccess.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PropertyFeatureServiceImp implements PropertyFeatureService {

    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private PropertyFeatureRepository propertyFeatureRepository;
    @Autowired
    private FeatureRepository featureRepository;

    @Override
    public ResponseEntity<PropertyFeature> savePropertyFeature(PropertyFeature propertyFeature) {
        Optional<Property> existProperty = this.propertyRepository.findById(propertyFeature.getPropertyFeatureKey().getProperty().getId());
        Optional<Feature> existFeature = this.featureRepository.findById(propertyFeature.getPropertyFeatureKey().getFeature().getId());

        if(existProperty.isPresent() && existFeature.isPresent()){

            Optional<PropertyFeature> existPropertyFeature = this.propertyFeatureRepository.findById(propertyFeature.getPropertyFeatureKey());

            if(existPropertyFeature.isPresent()){
                throw new PropertyFeatureAlreadyExistException();
            }else{
                PropertyFeature savedPropertyFeature = this.propertyFeatureRepository.save(propertyFeature);
                return new ResponseEntity<>(savedPropertyFeature, HttpStatus.CREATED);
            }

        }else if(!existFeature.isPresent()){
            throw new FeatureNotFoundException(existProperty.get().getId());

        }else{
            throw new PropertyNotFoundException(existProperty.get().getId());
        }
    }

    @Override
    public ResponseEntity<PropertyFeature> editPropertyFeature(PropertyFeature oldPropertyFeature, PropertyFeature newPropertyFeature) {
        Optional<PropertyFeature> existPropertyFeatureOpt = this.propertyFeatureRepository.findById(oldPropertyFeature.getPropertyFeatureKey());

        if(existPropertyFeatureOpt.isPresent()){
            Optional<Property> existProperty = this.propertyRepository.findById(newPropertyFeature.getPropertyFeatureKey().getProperty().getId());
            Optional<Feature> existFeature = this.featureRepository.findById(newPropertyFeature.getPropertyFeatureKey().getFeature().getId());

            if(existProperty.isPresent() && existFeature.isPresent()){

                this.propertyFeatureRepository.deleteById(oldPropertyFeature.getPropertyFeatureKey());
                PropertyFeature savedPropertyFeature = this.propertyFeatureRepository.save(newPropertyFeature);

                return new ResponseEntity<>(savedPropertyFeature, HttpStatus.CREATED);

            }else if(!existFeature.isPresent()){
                throw new FeatureNotFoundException(existProperty.get().getId());

            }else{
                throw new PropertyNotFoundException(existProperty.get().getId());
            }

        }else{
            throw new PropertyFeatureNotFoundException();
        }
    }

    @Override
    public ResponseEntity<PropertyFeature> deletePropertyFeature(PropertyFeature propertyFeature) {
        Optional<PropertyFeature> existPropertyFeature = this.propertyFeatureRepository.findById(propertyFeature.getPropertyFeatureKey());

        if(existPropertyFeature.isPresent()){
            this.propertyFeatureRepository.deleteById(propertyFeature.getPropertyFeatureKey());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }else{
            throw new PropertyFeatureNotFoundException();
        }
    }

    @Override
    public ResponseEntity<PropertyFeature> deleteAllPropertyFeature() {
        List<PropertyFeature> propertyFeatureList = this.propertyFeatureRepository.findAll();

        for(PropertyFeature propertyFeature : propertyFeatureList){
            deletePropertyFeature(propertyFeature);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<PropertyFeature>> searchPropertyFeature(PropertyFeature propertyFeature) {
        return null;
    }

    @Override
    public ResponseEntity<List<PropertyFeature>> getAllPropertyFeature() {
        List<PropertyFeature> propertyFeatureList = this.propertyFeatureRepository.findAll();

        if(propertyFeatureList.size() == 0){
            throw new DatabaseIsEmptyException();
        }else{
            return new ResponseEntity<>(propertyFeatureList, HttpStatus.OK);
        }
    }
}
