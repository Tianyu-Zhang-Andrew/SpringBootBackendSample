package com.mikason.PropView.services.documentService;

import com.mikason.PropView.Exception.databaseException.DatabaseIsEmptyException;
import com.mikason.PropView.Exception.databaseException.NoResultFoundException;
import com.mikason.PropView.Exception.documentEntityException.RentalAgreementAlreadyExistException;
import com.mikason.PropView.Exception.documentEntityException.RentalAgreementNotFoundException;
import com.mikason.PropView.Exception.commercialEntityException.RentNotFoundException;
import com.mikason.PropView.dataaccess.commercialEntity.Rent;
import com.mikason.PropView.dataaccess.documentEntity.RentalAgreement;
import com.mikason.PropView.dataaccess.repository.RentRepository;
import com.mikason.PropView.dataaccess.repository.RentalAgreementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RentalAgreementServiceImp implements RentalAgreementService{
    @Autowired
    private RentRepository rentRepository;
    @Autowired
    private RentalAgreementRepository rentalAgreementRepository;

    @Override
    public ResponseEntity<RentalAgreement> saveRentalAgreement(RentalAgreement rentalAgreement) {
        Optional<Rent> existRentOpt = this.rentRepository.findById(rentalAgreement.getRent().getRentKey());

        if(existRentOpt.isPresent()){
            List<RentalAgreement> rentalAgreementList = this.rentalAgreementRepository.findAll();
            for(RentalAgreement savedRentalAgreement: rentalAgreementList){
                if(savedRentalAgreement.getContent().equals(rentalAgreement.getContent()) && savedRentalAgreement.getRent().equals(rentalAgreement.getRent())){
                    throw new RentalAgreementAlreadyExistException();
                }
            }

            RentalAgreement savedRentalAgreement = this.rentalAgreementRepository.save(rentalAgreement);
            return new ResponseEntity<>(savedRentalAgreement, HttpStatus.CREATED);

        }else{
            throw new RentNotFoundException();
        }
    }

    @Override
    public ResponseEntity<RentalAgreement> editRentalAgreement(RentalAgreement rentalAgreement) {
        Optional<RentalAgreement> existRentalAgreementOpt = this.rentalAgreementRepository.findById(rentalAgreement.getId());
        if(existRentalAgreementOpt.isPresent()){

            Optional<Rent> newRentOpt = this.rentRepository.findById(rentalAgreement.getRent().getRentKey());
            if(newRentOpt.isPresent()) {
                Optional<Rent> oldRentOpt = this.rentRepository.findById(existRentalAgreementOpt.get().getRent().getRentKey());
                oldRentOpt.get().deleteRentalAgreement(existRentalAgreementOpt.get());
                this.rentRepository.save(oldRentOpt.get());

                RentalAgreement savedRentalAgreement = this.rentalAgreementRepository.save(rentalAgreement);
                return new ResponseEntity<>(savedRentalAgreement, HttpStatus.CREATED);

            }else {
                throw new RentNotFoundException();
            }

        }else{
            throw new RentalAgreementNotFoundException(rentalAgreement.getId());
        }
    }

    @Override
    public ResponseEntity<List<RentalAgreement>> searchRentalAgreement(RentalAgreement rentalAgreement) {
        Example<RentalAgreement> exampleRentalAgreement =  Example.of(rentalAgreement);
        List<RentalAgreement> rentalAgreementList = this.rentalAgreementRepository.findAll(exampleRentalAgreement);

        if(rentalAgreementList.size() == 0){
            throw new NoResultFoundException();
        }else {
            return new ResponseEntity<>(rentalAgreementList, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<RentalAgreement> deleteRentalAgreement(RentalAgreement rentalAgreement) {
        Optional<RentalAgreement> existRentalAgreementOpt = this.rentalAgreementRepository.findById(rentalAgreement.getId());
        if(existRentalAgreementOpt.isPresent()){
            Optional<Rent> existRentOpt = this.rentRepository.findById(rentalAgreement.getRent().getRentKey());
            if(existRentOpt.isPresent()) {
                this.rentalAgreementRepository.deleteById(rentalAgreement.getId());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            }else{
                throw new RentNotFoundException();
            }

        }else{
            throw new RentalAgreementNotFoundException(rentalAgreement.getId());
        }
    }

    @Override
    public ResponseEntity<RentalAgreement> deleteAllRentalAgreement() {
        List<RentalAgreement> rentalAgreementList = this.rentalAgreementRepository.findAll();

        for(RentalAgreement savedRentalAgreement : rentalAgreementList){
            deleteRentalAgreement(savedRentalAgreement);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<RentalAgreement>> getAllRentalAgreement() {
        List<RentalAgreement> rentalAgreementList = this.rentalAgreementRepository.findAll();

        if(rentalAgreementList.size() == 0){
            throw new DatabaseIsEmptyException();
        }else{
            return new ResponseEntity<>(rentalAgreementList, HttpStatus.OK);
        }
    }
}
