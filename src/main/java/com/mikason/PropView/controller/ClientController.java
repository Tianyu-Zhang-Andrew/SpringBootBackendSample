package com.mikason.PropView.controller;

import com.mikason.PropView.dataaccess.peopleEntity.Client;
import com.mikason.PropView.services.peopleService.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/client/saveClient")
    ResponseEntity<Client> saveClient(@RequestBody Client client){
        return clientService.saveClient(client);
    }

    @PostMapping("/client/editClient")
    ResponseEntity<Client> editClient(@RequestBody Client client){
        return clientService.editClient(client);
    }

    @PostMapping("/client/deleteClient")
    ResponseEntity<Client> deleteClient(@RequestBody Client client){
        return clientService.deleteClient(client);
    }

    @PostMapping("/client/deleteAllClient")
    ResponseEntity<Client> deleteAllClient(){
        return clientService.deleteAllClient();
    }

    @PostMapping("/client/searchClient")
    ResponseEntity<List<Client>> searchClienty(@RequestBody Client client){
        return clientService.searchProperty(client);
    }

    @GetMapping("/client/getAllClient")
    ResponseEntity<List<Client>> getAllClient(){
        return clientService.getAllClient();
    }
}
