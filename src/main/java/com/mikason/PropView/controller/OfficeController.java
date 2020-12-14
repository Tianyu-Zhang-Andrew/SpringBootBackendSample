package com.mikason.PropView.controller;

import com.mikason.PropView.dataaccess.estateEntity.Office;
import com.mikason.PropView.services.estateService.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OfficeController {

    @Autowired
    private OfficeService officeService;

    @PostMapping("/office/saveOffice")
    ResponseEntity<Office> saveOffice(@RequestBody Office office){
        return officeService.saveOffice(office);
    }

    @PostMapping("/office/editOffice")
    ResponseEntity<Office> editOffice(@RequestBody Office office){
        return officeService.editOffice(office);
    }

    @PostMapping("/office/deleteOffice")
    ResponseEntity<Office> deleteOffice(@RequestBody Office office){
        return officeService.deleteOffice(office);
    }

    @PostMapping("/office/deleteAllOffice")
    ResponseEntity<Office> deleteAllOffice(){
        return officeService.deleteAllOffice();
    }

    @PostMapping("/office/searchOffice")
    ResponseEntity<List<Office>> searchOffice(@RequestBody Office office){
        return officeService.searchOffice(office);
    }

    @GetMapping("/office/getAllOffice")
    ResponseEntity<List<Office>> getAllOffice(){
        return officeService.getAllOffice();
    }
}
