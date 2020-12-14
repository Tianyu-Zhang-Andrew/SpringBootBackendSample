package com.mikason.PropView.services.estateService;

import com.mikason.PropView.dataaccess.estateEntity.Office;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OfficeService {
    ResponseEntity<Office> saveOffice(Office office);
    ResponseEntity<Office> editOffice(Office office);
    ResponseEntity<Office> deleteOffice(Office office);
    ResponseEntity<Office> deleteAllOffice();
    ResponseEntity<List<Office>> searchOffice(Office office);
    ResponseEntity<List<Office>> getAllOffice();
}
