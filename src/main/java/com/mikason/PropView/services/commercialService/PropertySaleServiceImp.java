package com.mikason.PropView.services.commercialService;

import com.mikason.PropView.Exception.commercialEntityException.PropertySaleAlreadyExistException;
import com.mikason.PropView.Exception.commercialEntityException.PropertySaleNotFoundException;
import com.mikason.PropView.Exception.databaseException.DatabaseIsEmptyException;
import com.mikason.PropView.Exception.databaseException.NoResultFoundException;
import com.mikason.PropView.Exception.estateEntityException.PropertyNotFoundException;
import com.mikason.PropView.dataaccess.commercialEntity.PropertySale;
import com.mikason.PropView.dataaccess.estateEntity.Property;
import com.mikason.PropView.dataaccess.repository.PropertyRepository;
import com.mikason.PropView.dataaccess.repository.PropertySaleRepository;
import com.mikason.PropView.dataaccess.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PropertySaleServiceImp implements PropertySaleService{

    @Autowired
    private PropertySaleRepository propertySaleRepository;
    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private SaleRepository saleRepository;

    @Override
    public ResponseEntity<PropertySale> savePropertySale(PropertySale propertySale) {
        Optional<Property> existPropertyOpt = this.propertyRepository.findById(propertySale.getProperty().getId());

        if(existPropertyOpt.isPresent()){
            Example<PropertySale> propertySaleExample =  Example.of(propertySale);
            Optional<PropertySale> existPropSale = this.propertySaleRepository.findOne(propertySaleExample);
            if(existPropSale.isPresent()){
                throw new PropertySaleAlreadyExistException();

            }else{
                PropertySale savedPropertySale = this.propertySaleRepository.save(propertySale);
                return new ResponseEntity<>(savedPropertySale, HttpStatus.CREATED);
            }

        }else{
            throw new PropertyNotFoundException(propertySale.getProperty().getId());
        }
    }

    @Override
    public ResponseEntity<PropertySale> editPropertySale(PropertySale propertySale) {
        Optional<PropertySale> existPropertySaleOpt = this.propertySaleRepository.findById(propertySale.getId());

        if(existPropertySaleOpt.isPresent()){
//            PropertySale existPropertySale = existPropertySaleOpt.get();
//            Property oldProperty = existPropertySale.getProperty();
            Optional<Property> newPropertyOpt = this.propertyRepository.findById(propertySale.getProperty().getId());

            if(newPropertyOpt.isPresent()){
//                Property newProperty = newPropertyOpt.get();
//
//                oldProperty.deletePropertySale(existPropertySale);
//                this.propertyRepository.save(oldProperty);
//
//                newProperty.addPropertySale(propertySale);
//                this.propertyRepository.save(newProperty);
//
//                Example<PropertySale> savedPropertySaleExample =  Example.of(propertySale);
//                Optional<PropertySale> savedPropSale = this.propertySaleRepository.findOne(savedPropertySaleExample);
//
//                return new ResponseEntity<>(savedPropSale.get(), HttpStatus.CREATED);
                PropertySale savedPropertyRent = this.propertySaleRepository.save(propertySale);
                return new ResponseEntity<>(savedPropertyRent, HttpStatus.CREATED);

            }else{
                throw new PropertyNotFoundException(propertySale.getProperty().getId());
            }

        }else{
            throw new PropertySaleNotFoundException(propertySale.getId());
        }
    }

    @Override
    public ResponseEntity<PropertySale> deletePropertySale(PropertySale propertySale) {
        Optional<PropertySale> existPropertySaleOpt = this.propertySaleRepository.findById(propertySale.getId());

        if(existPropertySaleOpt.isPresent()){
            PropertySale existPropertySale = existPropertySaleOpt.get();

            Optional<Property> existPropertyOpt = this.propertyRepository.findById(propertySale.getProperty().getId());
            Property existProperty = existPropertyOpt.get();
            existProperty.deletePropertySale(existPropertySale);
            this.propertyRepository.save(existProperty);

            this.propertySaleRepository.deleteById(propertySale.getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }else{
            throw new PropertySaleNotFoundException(propertySale.getId());
        }
    }

    @Override
    public ResponseEntity<PropertySale> deleteAllPropertySale() {
        List<PropertySale> propertySaleList = this.propertySaleRepository.findAll();
        for(PropertySale propSale : propertySaleList){
            deletePropertySale(propSale);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<PropertySale>> searchPropertySale(PropertySale propertySale, Long lowSuggestedPriceLimit, Long highSuggestedPriceLimit) {
        List<PropertySale> propList;

        //Search propertySale with some property restriction
        if(propertySale != null) {
            Example<PropertySale> propertySaleExample =  Example.of(propertySale);
            propList = this.propertySaleRepository.findAll(propertySaleExample);

            if(propList.size() == 0){
                throw new NoResultFoundException();
            }

            //Search all propertySale with no property restriction
        }else{
            propList = this.propertySaleRepository.findAll();
        }

        //Search propertySale with propertySale with only property restriction
        if(lowSuggestedPriceLimit == null && highSuggestedPriceLimit == null){
            return new ResponseEntity<>(propList, HttpStatus.OK);

            //Search propertySale in a suggested price range
        }else if(lowSuggestedPriceLimit != null && highSuggestedPriceLimit != null){
            List <PropertySale> resultList = new ArrayList<>();
            for(PropertySale propSale : propList){
                if(propSale.getSuggestedSalesPrice() <= highSuggestedPriceLimit && propSale.getSuggestedSalesPrice() >= lowSuggestedPriceLimit){
                    resultList.add(propSale);
                }
            }

            if(resultList.size() == 0){
                throw new NoResultFoundException();
            }else{
                return new ResponseEntity<>(resultList, HttpStatus.OK);
            }

            //Search propertySale with a lowest suggested price restriction
        }else if(lowSuggestedPriceLimit != null){
            List <PropertySale> resultList = new ArrayList<>();
            for(PropertySale propSale : propList){
                if(propSale.getSuggestedSalesPrice() >= lowSuggestedPriceLimit){
                    resultList.add(propSale);
                }
            }

            if(resultList.size() == 0){
                throw new NoResultFoundException();
            }else{
                return new ResponseEntity<>(resultList, HttpStatus.OK);
            }

            //Search propertySale with a highest suggested price restriction
        }else {
            List <PropertySale> resultList = new ArrayList<>();
            for(PropertySale propSale : propList){
                if(propSale.getSuggestedSalesPrice() <= highSuggestedPriceLimit){
                    resultList.add(propSale);
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
    public ResponseEntity<List<PropertySale>> getAllPropertySale() {
        List<PropertySale> propertySalesList = this.propertySaleRepository.findAll();

        if(propertySalesList.size() == 0){
            throw new DatabaseIsEmptyException();
        }else{
            return new ResponseEntity<>(propertySalesList, HttpStatus.OK);
        }
    }
}
