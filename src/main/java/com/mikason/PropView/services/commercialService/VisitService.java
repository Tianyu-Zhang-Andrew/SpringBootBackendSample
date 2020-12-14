package com.mikason.PropView.services.commercialService;

import com.mikason.PropView.dataaccess.commercialEntity.Visit;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

public interface VisitService {
    ResponseEntity<Visit> saveVisit(Visit visit);
    ResponseEntity<Visit> editVisit(Visit visit);
    ResponseEntity<Visit> deleteVisit(Visit visit);
    ResponseEntity<List<Visit>> searchVisit(Visit visit, Date start, Date end);
    ResponseEntity<Visit> deleteAllVisit();
    ResponseEntity<List<Visit>> getAllVisit();
}
