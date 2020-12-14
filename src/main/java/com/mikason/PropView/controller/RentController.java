package com.mikason.PropView.controller;

import com.google.gson.Gson;
import com.mikason.PropView.dataaccess.commercialEntity.Rent;
import com.mikason.PropView.services.commercialService.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RentController {
    @Autowired
    private RentService rentService;

    @PostMapping("/rent/saveRent")
    ResponseEntity<Rent> saveRent(@RequestBody Rent rent){
        return rentService.saveRent(rent);
    }

    @PostMapping("/rent/editRent")
    ResponseEntity<Rent> editRent(@RequestParam(value = "oldRent", required=false) String oldRentStr,
                                  @RequestParam(value = "newRent",required=false) String newRentStr){

        Gson gson = new Gson();
        Rent oldRent = gson.fromJson(oldRentStr, Rent.class);
        Rent newRent = gson.fromJson(newRentStr, Rent.class);

        return rentService.editRent(oldRent, newRent);
    }

    @PostMapping("/rent/deleteRent")
    ResponseEntity<Rent> deleteRent(@RequestBody Rent rent){
        return rentService.deleteRent(rent);
    }

    @PostMapping("/rent/deleteAllRent")
    ResponseEntity<Rent> deleteAllRent(){
        return rentService.deleteAllRent();
    }

    @GetMapping("/rent/getAllRent")
    ResponseEntity<List<Rent>> getAllRent(){
        return rentService.getAllRent();
    }
}
