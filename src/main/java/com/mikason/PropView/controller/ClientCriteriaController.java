package com.mikason.PropView.controller;

import com.google.gson.Gson;
import com.mikason.PropView.dataaccess.commercialEntity.ClientCriteria;
import com.mikason.PropView.services.commercialService.ClientCriteriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ClientCriteriaController {

    @Autowired
    private ClientCriteriaService clientCriteriaService;

    @PostMapping("/clientCriteria/saveClientCriteria")
    ResponseEntity<ClientCriteria> saveClientCriteria(@RequestBody ClientCriteria clientCriteria){
        return clientCriteriaService.saveClientCriteria(clientCriteria);
    }

    @PostMapping("/clientCriteria/editClientCriteria")
    ResponseEntity<ClientCriteria> editClientCriteria(@RequestParam(value = "oldClientCriteria", required=false) String oldClientCriteriaStr,
                                                      @RequestParam(value = "newClientCriteria",required=false) String newClientCriteriaStr){

        Gson gson = new Gson();
        ClientCriteria oldClientCriteria = gson.fromJson(oldClientCriteriaStr, ClientCriteria.class);
        ClientCriteria newClientCriteria = gson.fromJson(newClientCriteriaStr, ClientCriteria.class);

        return clientCriteriaService.editClientCriteria(oldClientCriteria, newClientCriteria);
    }

    @PostMapping("/clientCriteria/deleteClientCriteria")
    ResponseEntity<ClientCriteria> deleteClientCriteria(@RequestBody ClientCriteria clientCriteria){
        return clientCriteriaService.deleteClientCriteria(clientCriteria);
    }

    @PostMapping("/clientCriteria/deleteAllClientCriteria")
    ResponseEntity<ClientCriteria> deleteAllClientCriteria(){
        return clientCriteriaService.deleteAllClientCriteria();
    }

    @PostMapping("/clientCriteria/searchClientCriteria")
    ResponseEntity<List<ClientCriteria>> searchClientCriteria(@RequestBody ClientCriteria clientCriteria){
        return clientCriteriaService.searchClientCriteria(clientCriteria);
    }

    @GetMapping("/clientCriteria/getAllClientCriteria")
    ResponseEntity<List<ClientCriteria>> getAllSale(){
        return clientCriteriaService.getAllClientCriteria();
    }
}
