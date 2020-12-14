package com.mikason.PropView.services.commercialService;

import com.mikason.PropView.dataaccess.commercialEntity.Rent;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RentService {
    ResponseEntity<Rent> saveRent(Rent rent);
    ResponseEntity<Rent> deleteRent(Rent rent);
    ResponseEntity<Rent> editRent(Rent oldRent, Rent newRent);
    ResponseEntity<Rent> deleteAllRent();
    ResponseEntity<List<Rent>> getAllRent();
}
