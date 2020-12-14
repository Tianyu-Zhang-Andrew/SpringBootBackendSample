package com.mikason.PropView.controller;

import com.mikason.PropView.dataaccess.documentEntity.RentalAgreement;
import com.mikason.PropView.services.documentService.RentalAgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RentalAgreementController {
    @Autowired
    private RentalAgreementService rentalAgreementService;

    @PostMapping("/rentalAgreement/saveRentalAgreement")
    ResponseEntity<RentalAgreement> saveRentalAgreement(@RequestBody RentalAgreement rentalAgreement){
        return rentalAgreementService.saveRentalAgreement(rentalAgreement);
    }

    @PostMapping("/rentalAgreement/editRentalAgreement")
    ResponseEntity<RentalAgreement> editRentalAgreement(@RequestBody RentalAgreement rentalAgreement){
        return rentalAgreementService.editRentalAgreement(rentalAgreement);
    }

    @PostMapping("/rentalAgreement/searchRentalAgreement")
    ResponseEntity<List<RentalAgreement>> searchRentalAgreement(@RequestBody RentalAgreement rentalAgreement){
        return rentalAgreementService.searchRentalAgreement(rentalAgreement);
    }

    @PostMapping("/rentalAgreement/deleteRentalAgreement")
    ResponseEntity<RentalAgreement> deleteRentalAgreement(@RequestBody RentalAgreement rentalAgreement){
        return rentalAgreementService.deleteRentalAgreement(rentalAgreement);
    }

    @PostMapping("/rentalAgreement/deleteAllRentalAgreement")
    ResponseEntity<RentalAgreement> deleteAllRentalAgreement(){
        return rentalAgreementService.deleteAllRentalAgreement();
    }

    @GetMapping("/rentalAgreement/getAllRentalAgreement")
    ResponseEntity<List<RentalAgreement>> getAllRentalAgreement(){
        return rentalAgreementService.getAllRentalAgreement();
    }
}
