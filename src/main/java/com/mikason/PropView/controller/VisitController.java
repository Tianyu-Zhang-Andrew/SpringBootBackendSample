package com.mikason.PropView.controller;

import com.google.gson.Gson;
import com.mikason.PropView.dataaccess.commercialEntity.Visit;
import com.mikason.PropView.services.commercialService.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class VisitController {

    @Autowired
    private VisitService visitService;

    @PostMapping("/visit/saveVisit")
    ResponseEntity<Visit> saveVisit(@RequestBody Visit visit){
        return visitService.saveVisit(visit);
    }

    @PostMapping("/visit/editVisit")
    ResponseEntity<Visit> editAgent(@RequestBody Visit visit){
        return visitService.editVisit(visit);
    }

    @PostMapping("/visit/deleteVisit")
    ResponseEntity<Visit> deleteVisit(@RequestBody Visit visit){
        return visitService.deleteVisit(visit);
    }

    @PostMapping("/visit/searchVisit")
    ResponseEntity<List<Visit>> searchVisit(@RequestParam(value = "visit", required=false) String visit,
                                            @RequestParam(value = "start",required=false) String start,
                                            @RequestParam(value = "end", required=false) String end){

        Gson gson = new Gson();
        Visit receivedVisitObj = gson.fromJson(visit, Visit.class);

        Date startObj = gson.fromJson(start, Date.class);
        Date endObj = gson.fromJson(end, Date.class);

        return visitService.searchVisit(receivedVisitObj, startObj, endObj);
    }

    @PostMapping("/visit/deleteAllVisit")
    ResponseEntity<Visit> deleteAllVisit(){
        return visitService.deleteAllVisit();
    }

    @GetMapping("/visit/getAllVisit")
    ResponseEntity<List<Visit>> getAllVisit(){
        return visitService.getAllVisit();
    }
}
