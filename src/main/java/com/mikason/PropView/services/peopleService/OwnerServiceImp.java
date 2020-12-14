package com.mikason.PropView.services.peopleService;

import com.mikason.PropView.Exception.databaseException.DatabaseIsEmptyException;
import com.mikason.PropView.Exception.databaseException.NoResultFoundException;
import com.mikason.PropView.Exception.peopleEntityException.OwnerAlreadyExistException;
import com.mikason.PropView.Exception.peopleEntityException.OwnerNotFoundException;
import com.mikason.PropView.dataaccess.peopleEntity.Owner;
import com.mikason.PropView.dataaccess.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OwnerServiceImp implements OwnerService{

    @Autowired
    private OwnerRepository ownerRespository;

    @Override
    public ResponseEntity<Owner> saveOwner(Owner owner) {
        Example<Owner> ownerExample =  Example.of(owner);
        Optional<Owner> existOwner= this.ownerRespository.findOne(ownerExample);
        if(existOwner.isPresent()) {
            throw new OwnerAlreadyExistException();
        }else {
            Owner savedOwner = this.ownerRespository.save(owner);
            return new ResponseEntity<>(savedOwner, HttpStatus.CREATED);
        }
    }

    @Override
    public ResponseEntity<Owner> editOwner(Owner owner) {
        Optional<Owner> existOwner = this.ownerRespository.findById(owner.getId());

        if(!existOwner.isPresent()) {
            throw new OwnerNotFoundException(owner.getId());
        }else {
            Owner savedOwner = this.ownerRespository.save(owner);
            return new ResponseEntity<>(savedOwner, HttpStatus.CREATED);
        }
    }

    @Override
    public ResponseEntity<Owner> deleteOwner(Owner owner) {
        Optional<Owner> existOwner = this.ownerRespository.findById(owner.getId());

        if(!existOwner.isPresent()) {
            throw new OwnerNotFoundException(owner.getId());
        }else {
            this.ownerRespository.delete(owner);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @Override
    public ResponseEntity<Owner> deleteAllOwner() {
        this.ownerRespository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<Owner>> searchOwner(Owner owner) {
        Example<Owner> ownerExample =  Example.of(owner);
        List<Owner> ownerList = this.ownerRespository.findAll(ownerExample);

        if(ownerList.size() == 0){
            throw new NoResultFoundException();
        }else{
            return new ResponseEntity<>(ownerList, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<List<Owner>> getAllOwner() {
        List<Owner> ownerList = this.ownerRespository.findAll();

        if(ownerList.size() == 0){
            throw new DatabaseIsEmptyException();
        }else{
            return new ResponseEntity<>(ownerList, HttpStatus.OK);
        }
    }
}
