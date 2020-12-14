package com.mikason.PropView.controller;

import com.mikason.PropView.dataaccess.peopleEntity.Owner;
import com.mikason.PropView.services.peopleService.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OwnerController {
    @Autowired
    private OwnerService ownerService;

    @PostMapping("/owner/saveOwner")
    ResponseEntity<Owner> saveOwner(@RequestBody Owner owner){
        return ownerService.saveOwner(owner);
    }

    @PostMapping("/owner/editOwner")
    ResponseEntity<Owner> editOwner(@RequestBody Owner owner){
        return ownerService.editOwner(owner);
    }

    @PostMapping("/owner/deleteOwner")
    ResponseEntity<Owner> deleteOwner(@RequestBody Owner owner){
        return ownerService.deleteOwner(owner);
    }

    @PostMapping("/owner/deleteAllOwner")
    ResponseEntity<Owner> deleteAllOwner(){
        return ownerService.deleteAllOwner();
    }

    @PostMapping("/owner/searchOwner")
    ResponseEntity<List<Owner>> searchOwner(@RequestBody Owner owner){
        return ownerService.searchOwner(owner);
    }

    @GetMapping("/owner/getAllOwner")
    ResponseEntity<List<Owner>> getAllOwner(){
        return ownerService.getAllOwner();
    }
}
