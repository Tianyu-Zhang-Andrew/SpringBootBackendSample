package com.mikason.PropView.services.peopleService;

import com.mikason.PropView.dataaccess.peopleEntity.Client;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ClientService {
    ResponseEntity<Client> saveClient(Client client);
    ResponseEntity<Client> editClient(Client client);
    ResponseEntity<Client> deleteClient(Client client);
    ResponseEntity<Client> deleteAllClient();
    ResponseEntity<List<Client>> searchProperty(Client client);
    ResponseEntity<List<Client>> getAllClient();
}
