package com.mikason.PropView.services.estateService;

import com.mikason.PropView.Exception.databaseException.DatabaseIsEmptyException;
import com.mikason.PropView.Exception.databaseException.NoResultFoundException;
import com.mikason.PropView.Exception.estateEntityException.PropertyAlreadyExistException;
import com.mikason.PropView.Exception.estateEntityException.PropertyNotFoundException;
import com.mikason.PropView.dataaccess.estateEntity.Property;
import com.mikason.PropView.dataaccess.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PropertyServiceImp implements PropertyService{

    @Autowired
    private PropertyRepository propertyRepository;

    @Override
    public ResponseEntity<Property> saveProperty(Property property) {
        Example<Property> propertyExample =  Example.of(property);
        Optional<Property> existProp = this.propertyRepository.findOne(propertyExample);

        if(existProp.isPresent()) {
            throw new PropertyAlreadyExistException();
        }else {
            Property savedProp = this.propertyRepository.save(property);
            return new ResponseEntity<>(savedProp, HttpStatus.CREATED);
        }
    }

    @Override
    public ResponseEntity<Property> editProperty(Property property){
        Optional<Property> existProp = this.propertyRepository.findById(property.getId());

        if(!existProp.isPresent()) {
            throw new PropertyNotFoundException(property.getId());
        }else {
            long addressId = existProp.get().getAddress().getId();
            property.getAddress().setId(addressId);

            Property savedProp = this.propertyRepository.save(property);
            return new ResponseEntity<>(savedProp, HttpStatus.CREATED);
        }
    }

    @Override
    public ResponseEntity<Property> deleteProperty(Property property){
        Optional<Property> existProp = this.propertyRepository.findById(property.getId());

        if(!existProp.isPresent()) {
            throw new PropertyNotFoundException(property.getId());
        }else {
            this.propertyRepository.delete(property);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @Override
    public ResponseEntity<Property> deleteAllProperty(){
        this.propertyRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<Property>> searchProperty(Property property){
        Example<Property> propertyExample =  Example.of(property);
        List<Property> propList = this.propertyRepository.findAll(propertyExample);

        if(propList.size() == 0){
            throw new NoResultFoundException();
        }else {
            return new ResponseEntity<>(propList, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<List<Property>> getAllProperty(){
        List<Property> propList = this.propertyRepository.findAll();

        if(propList.size() == 0){
            throw new DatabaseIsEmptyException();
        }else{
            return new ResponseEntity<>(propList, HttpStatus.OK);
        }
    }
}
