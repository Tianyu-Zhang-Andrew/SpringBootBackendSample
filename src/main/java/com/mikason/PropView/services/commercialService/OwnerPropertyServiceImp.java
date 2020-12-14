package com.mikason.PropView.services.commercialService;

import com.mikason.PropView.Exception.commercialEntityException.OwnerPropertyNotFoundException;
import com.mikason.PropView.Exception.databaseException.DatabaseIsEmptyException;
import com.mikason.PropView.Exception.databaseException.NoResultFoundException;
import com.mikason.PropView.Exception.estateEntityException.PropertyNotFoundException;
import com.mikason.PropView.Exception.peopleEntityException.OwnerNotFoundException;
import com.mikason.PropView.dataaccess.commercialEntity.OwnerProperty;
import com.mikason.PropView.dataaccess.estateEntity.Property;
import com.mikason.PropView.dataaccess.peopleEntity.Owner;
import com.mikason.PropView.dataaccess.repository.OwnerPropertyRepository;
import com.mikason.PropView.dataaccess.repository.OwnerRepository;
import com.mikason.PropView.dataaccess.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OwnerPropertyServiceImp implements OwnerPropertyService{

    @Autowired
    private OwnerPropertyRepository ownerPropertyRespository;
    @Autowired
    private OwnerRepository ownerRespository;
    @Autowired
    private PropertyRepository propertyRespository;

    @Override
    public ResponseEntity<OwnerProperty> saveOwnerProperty(OwnerProperty ownerProperty) {
        Optional<Owner> existOwner = this.ownerRespository.findById(ownerProperty.getOwnerPropertyKey().getOwner().getId());
        Optional<Property> existProperty = this.propertyRespository.findById(ownerProperty.getOwnerPropertyKey().getProperty().getId());

        if(existOwner.isPresent() && existProperty.isPresent()){
            OwnerProperty savedOwnerProperty = this.ownerPropertyRespository.save(ownerProperty);
            return new ResponseEntity<>(savedOwnerProperty, HttpStatus.CREATED);

        }else if(!existOwner.isPresent()){
            throw new OwnerNotFoundException(existOwner.get().getId());
        }else{
            throw new PropertyNotFoundException(existProperty.get().getId());
        }
    }

    @Override
    public ResponseEntity<OwnerProperty> editOwnerProperty(OwnerProperty oldOwnerProperty, OwnerProperty newOwnerProperty) {
        Optional<Owner> oldOwner = this.ownerRespository.findById(oldOwnerProperty.getOwnerPropertyKey().getOwner().getId());
        Optional<Property> oldProperty = this.propertyRespository.findById(oldOwnerProperty.getOwnerPropertyKey().getProperty().getId());

        if(oldOwner.isPresent() && oldProperty.isPresent()){
            Optional<Owner> newOwner = this.ownerRespository.findById(newOwnerProperty.getOwnerPropertyKey().getOwner().getId());
            Optional<Property> newProperty = this.propertyRespository.findById(newOwnerProperty.getOwnerPropertyKey().getProperty().getId());

            if(newOwner.isPresent() && newProperty.isPresent()){
                this.ownerPropertyRespository.deleteById(oldOwnerProperty.getOwnerPropertyKey());
                OwnerProperty savedOwnerProperty = this.ownerPropertyRespository.save(newOwnerProperty);
                return new ResponseEntity<>(savedOwnerProperty, HttpStatus.CREATED);

            }else if(!newOwner.isPresent()){
                throw new OwnerNotFoundException(newOwner.get().getId());
            }else{
                throw new PropertyNotFoundException(newProperty.get().getId());
            }

        }else if(!oldOwner.isPresent()){
            throw new OwnerNotFoundException(oldOwner.get().getId());
        }else{
            throw new PropertyNotFoundException(oldProperty.get().getId());
        }
    }

    @Override
    public ResponseEntity<OwnerProperty> deleteOwnerProperty(OwnerProperty ownerProperty) {
        Optional<Owner> existOwner = this.ownerRespository.findById(ownerProperty.getOwnerPropertyKey().getOwner().getId());
        Optional<Property> existProperty = this.propertyRespository.findById(ownerProperty.getOwnerPropertyKey().getProperty().getId());

        if(existOwner.isPresent() && existProperty.isPresent()){
            Optional<OwnerProperty> existOwnerPropertyOpt = this.ownerPropertyRespository.findById(ownerProperty.getOwnerPropertyKey());
            if(existOwnerPropertyOpt.isPresent()) {
                this.ownerPropertyRespository.deleteById(ownerProperty.getOwnerPropertyKey());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            }else{
                throw new OwnerPropertyNotFoundException();
            }

        }else if(!existOwner.isPresent()){
            throw new OwnerNotFoundException(existOwner.get().getId());
        }else{
            throw new PropertyNotFoundException(existProperty.get().getId());
        }
    }

    @Override
    public ResponseEntity<OwnerProperty> deleteAllOwnerProperty() {
        List<OwnerProperty> ownerPropertyList = this.ownerPropertyRespository.findAll();

        for(OwnerProperty savedOwnerProperty : ownerPropertyList){
            deleteOwnerProperty(savedOwnerProperty);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<OwnerProperty>> searchOwnerProperty(OwnerProperty ownerProperty) {
        Example<OwnerProperty> ownerPropertyExample =  Example.of(ownerProperty);
        List<OwnerProperty> ownerPropertyList = this.ownerPropertyRespository.findAll(ownerPropertyExample);

        if(ownerPropertyList.size() == 0){
            throw new NoResultFoundException();
        }else {
            return new ResponseEntity<>(ownerPropertyList, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<List<OwnerProperty>> getAllOwnerProperty() {
        List<OwnerProperty> ownerPropertiesList = this.ownerPropertyRespository.findAll();

        if(ownerPropertiesList.size() == 0){
            throw new DatabaseIsEmptyException();
        }else{
            return new ResponseEntity<>(ownerPropertiesList, HttpStatus.OK);
        }
    }
}
