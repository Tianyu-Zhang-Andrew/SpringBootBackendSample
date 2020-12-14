package com.mikason.PropView.services.documentService;

import com.mikason.PropView.dataaccess.documentEntity.RentalAgreement;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RentalAgreementService {
    ResponseEntity<RentalAgreement> saveRentalAgreement(RentalAgreement rentalAgreement);
    ResponseEntity<RentalAgreement> editRentalAgreement(RentalAgreement rentalAgreement);
    ResponseEntity<List<RentalAgreement>> searchRentalAgreement(RentalAgreement rentalAgreement);
    ResponseEntity<RentalAgreement> deleteRentalAgreement(RentalAgreement rentalAgreement);
    ResponseEntity<RentalAgreement> deleteAllRentalAgreement();
    ResponseEntity<List<RentalAgreement>> getAllRentalAgreement();
}
