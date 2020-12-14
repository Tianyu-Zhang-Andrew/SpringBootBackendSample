package com.mikason.PropView.services.commercialService;

import com.mikason.PropView.Exception.commercialEntityException.PropertyRentAlreadyExistException;
import com.mikason.PropView.Exception.commercialEntityException.PropertyRentNotFoundException;
import com.mikason.PropView.Exception.databaseException.DatabaseIsEmptyException;
import com.mikason.PropView.Exception.databaseException.NoResultFoundException;
import com.mikason.PropView.Exception.estateEntityException.PropertyNotFoundException;
import com.mikason.PropView.dataaccess.commercialEntity.PropertyRent;
import com.mikason.PropView.dataaccess.estateEntity.Property;
import com.mikason.PropView.dataaccess.repository.PropertyRentRepository;
import com.mikason.PropView.dataaccess.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PropertyRentSeviceImp implements PropertyRentService {

    @Autowired
    private PropertyRentRepository PropertyRentRespository;
    @Autowired
    private PropertyRepository propertyRepository;

    @Override
    public ResponseEntity<PropertyRent> savePropertyRent(PropertyRent propertyRent) {
        Optional<Property> existPropertyOpt = this.propertyRepository.findById(propertyRent.getProperty().getId());

        if(existPropertyOpt.isPresent()){
            Example<PropertyRent> propertyRentExample =  Example.of(propertyRent);
            Optional<PropertyRent> existPropRent = this.PropertyRentRespository.findOne(propertyRentExample);

            if(existPropRent.isPresent()){
                throw new PropertyRentAlreadyExistException();
            }else{
                PropertyRent savedPropertyRent = this.PropertyRentRespository.save(propertyRent);
                return new ResponseEntity<>(savedPropertyRent, HttpStatus.CREATED);
            }

        }else{
            throw new PropertyNotFoundException(propertyRent.getProperty().getId());
        }
    }

    @Override
    public ResponseEntity<PropertyRent> editPropertyRent(PropertyRent propertyRent) {
        Optional<PropertyRent> existPropertyRentOpt = this.PropertyRentRespository.findById(propertyRent.getId());

        if(existPropertyRentOpt.isPresent()){
            PropertyRent existPropertyRent = existPropertyRentOpt.get();
            Property oldProperty = existPropertyRent.getProperty();
            Optional<Property> newPropertyOpt = this.propertyRepository.findById(propertyRent.getProperty().getId());

            if(newPropertyOpt.isPresent()){
//                Property newProperty = newPropertyOpt.get();
//
//                oldProperty.deletePropertyRent(existPropertyRent);
//                this.propertyRepository.save(oldProperty);
//
//                newProperty.addPropertyRent(propertyRent);
//                this.propertyRepository.save(newProperty);
//
//                Example<PropertyRent> savedPropertyRentExample =  Example.of(propertyRent);
//                Optional<PropertyRent> savedPropRent = this.PropertyRentRespository.findOne(savedPropertyRentExample);
//
//                return new ResponseEntity<>(savedPropRent.get(), HttpStatus.OK);

                PropertyRent savedPropertyRent = this.PropertyRentRespository.save(propertyRent);
                return new ResponseEntity<>(savedPropertyRent, HttpStatus.CREATED);

            }else{
                throw new PropertyNotFoundException(propertyRent.getProperty().getId());
            }

        }else{
            throw new PropertyRentNotFoundException(propertyRent.getId());
        }
    }

    @Override
    public ResponseEntity<List<PropertyRent>> searchPropertyRent(PropertyRent propertyRent, Long lowAvailablePriceLimit, Long highAvailablePriceLimit) {
        List<PropertyRent> propList;

        //Search propertyRent with some property restriction
        if(propertyRent != null) {
            Example<PropertyRent> propertyRentExample =  Example.of(propertyRent);
            propList = this.PropertyRentRespository.findAll(propertyRentExample);

            if(propList.size() == 0){
                throw new NoResultFoundException();
            }

            //Search all propertyRent with no property restriction
        }else{
            propList = this.PropertyRentRespository.findAll();
        }

        //Search propertyRent with propertyRent with only property restriction
        if(lowAvailablePriceLimit == null && highAvailablePriceLimit == null){
            return new ResponseEntity<>(propList, HttpStatus.OK);

            //Search propertyRent in a suggested price range
        }else if(lowAvailablePriceLimit != null && highAvailablePriceLimit != null){
            List <PropertyRent> resultList = new ArrayList<>();
            for(PropertyRent propRent : propList){
                if(propRent.getSuggestedRentalPrice() <= highAvailablePriceLimit && propRent.getSuggestedRentalPrice() >= lowAvailablePriceLimit){
                    resultList.add(propRent);
                }
            }

            if(resultList.size() == 0){
                throw new NoResultFoundException();
            }else{
                return new ResponseEntity<>(resultList, HttpStatus.OK);
            }

            //Search propertyRent with a lowest suggested price restriction
        }else if(lowAvailablePriceLimit != null){
            List <PropertyRent> resultList = new ArrayList<>();
            for(PropertyRent propRent : propList){
                if(propRent.getSuggestedRentalPrice() >= lowAvailablePriceLimit){
                    resultList.add(propRent);
                }
            }

            if(resultList.size() == 0){
                throw new NoResultFoundException();
            }else{
                return new ResponseEntity<>(resultList, HttpStatus.OK);
            }

            //Search propertyRent with a highest suggested price restriction
        }else {
            List <PropertyRent> resultList = new ArrayList<>();
            for(PropertyRent propRent : propList){
                if(propRent.getSuggestedRentalPrice() <= highAvailablePriceLimit){
                    resultList.add(propRent);
                }
            }

            if(resultList.size() == 0){
                throw new NoResultFoundException();
            }else{
                return new ResponseEntity<>(resultList, HttpStatus.OK);
            }
        }
    }

    @Override
    public ResponseEntity<List<PropertyRent>> getAllPropertyRent() {
        List<PropertyRent> propertyRentList = this.PropertyRentRespository.findAll();

        if(propertyRentList.size() == 0){
            throw new DatabaseIsEmptyException();
        }else{
            return new ResponseEntity<>(propertyRentList, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<PropertyRent> deleteAllPropertyRent() {
        List<PropertyRent> propertyRentList = this.PropertyRentRespository.findAll();
        for(PropertyRent propRent : propertyRentList){
            deletePropertyRent(propRent);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<PropertyRent> deletePropertyRent(PropertyRent propertyRent) {
        Optional<PropertyRent> existPropertyRentOpt = this.PropertyRentRespository.findById(propertyRent.getId());

        if(existPropertyRentOpt.isPresent()){
            PropertyRent existPropertyRent = existPropertyRentOpt.get();

            Optional<Property> existPropertyOpt = this.propertyRepository.findById(propertyRent.getProperty().getId());
            Property existProperty = existPropertyOpt.get();
            existProperty.deletePropertyRent(existPropertyRent);
            this.propertyRepository.save(existProperty);

            this.PropertyRentRespository.deleteById(propertyRent.getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }else{
            throw new PropertyRentNotFoundException(propertyRent.getId());
        }
    }
}
