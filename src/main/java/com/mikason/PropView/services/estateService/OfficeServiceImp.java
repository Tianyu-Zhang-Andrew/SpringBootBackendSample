package com.mikason.PropView.services.estateService;

import com.mikason.PropView.Exception.estateEntityException.OfficeAlreadyExistException;
import com.mikason.PropView.Exception.estateEntityException.OfficeNotFoundException;
import com.mikason.PropView.Exception.databaseException.DatabaseIsEmptyException;
import com.mikason.PropView.Exception.databaseException.NoResultFoundException;
import com.mikason.PropView.dataaccess.estateEntity.Office;
import com.mikason.PropView.dataaccess.repository.OfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OfficeServiceImp implements OfficeService {

    @Autowired
    private OfficeRepository officeRepository;

    @Override
    public ResponseEntity<Office> saveOffice(Office office) {
        Example<Office> officeExample =  Example.of(office);
        Optional<Office> existOffice = this.officeRepository.findOne(officeExample);

        if(existOffice.isPresent()) {
            throw new OfficeAlreadyExistException();
        }else {
            Office savedOffice = this.officeRepository.save(office);
            return new ResponseEntity<>(savedOffice, HttpStatus.CREATED);
        }
    }

    @Override
    public ResponseEntity<Office> editOffice(Office office) {
        Optional<Office> existOffice = this.officeRepository.findById(office.getId());

        if(!existOffice.isPresent()) {
            throw new OfficeNotFoundException(office.getId());
        }else {
            long addressId = existOffice.get().getAddress().getId();
            office.getAddress().setId(addressId);

            Office savedOffice = this.officeRepository.save(office);
            return new ResponseEntity<>(savedOffice, HttpStatus.CREATED);
        }
    }

    @Override
    public ResponseEntity<Office> deleteOffice(Office office) {
        Optional<Office> existOffice = this.officeRepository.findById(office.getId());

        if(!existOffice.isPresent()) {
            throw new OfficeNotFoundException(office.getId());
        }else {
            this.officeRepository.delete(office);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @Override
    public ResponseEntity<Office> deleteAllOffice() {
        this.officeRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<Office>> searchOffice(Office office) {
        Example<Office> officeExample =  Example.of(office);
        List<Office> officeList = this.officeRepository.findAll(officeExample);

        if(officeList.size() == 0){
            throw new NoResultFoundException();
        }else {
            return new ResponseEntity<>(officeList, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<List<Office>> getAllOffice() {
        List<Office> officeList = this.officeRepository.findAll();

        if(officeList.size() == 0){
            throw new DatabaseIsEmptyException();
        }else{
            return new ResponseEntity<>(officeList, HttpStatus.OK);
        }
    }
}
