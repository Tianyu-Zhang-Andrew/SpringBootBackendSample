package com.mikason.PropView.services.peopleService;

import com.mikason.PropView.Exception.peopleEntityException.ClientAlreadyExistException;
import com.mikason.PropView.Exception.peopleEntityException.ClientNotFoundException;
import com.mikason.PropView.Exception.databaseException.DatabaseIsEmptyException;
import com.mikason.PropView.Exception.databaseException.NoResultFoundException;
import com.mikason.PropView.dataaccess.peopleEntity.Client;
import com.mikason.PropView.dataaccess.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImp implements ClientService{
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public ResponseEntity<Client> saveClient(Client client){
        Example<Client> clientExample =  Example.of(client);
        Optional<Client> existClient= this.clientRepository.findOne(clientExample);
        if(existClient.isPresent()) {
            throw new ClientAlreadyExistException();
        }else {
            Client savedClient = this.clientRepository.save(client);
            return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
        }
    }

    @Override
    public ResponseEntity<Client> editClient(Client client){
        Optional<Client> existClient = this.clientRepository.findById(client.getId());

        if(!existClient.isPresent()) {
            throw new ClientNotFoundException(client.getId());
        }else {
            long addressId = existClient.get().getAddress().getId();
            long personId = existClient.get().getPerson().getId();
            client.getAddress().setId(addressId);
            client.getPerson().setId(personId);

            Client savedClient = this.clientRepository.save(client);
            return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
        }
    }

    @Override
    public ResponseEntity<Client> deleteClient(Client client){
        Optional<Client> existClient = this.clientRepository.findById(client.getId());

        if(!existClient.isPresent()) {
            throw new ClientNotFoundException(client.getId());
        }else {
            this.clientRepository.delete(client);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @Override
    public ResponseEntity<Client> deleteAllClient(){
        this.clientRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<Client>> searchProperty(Client client){
        Example<Client> clientExample =  Example.of(client);
        List<Client> clientList = this.clientRepository.findAll(clientExample);

        if(clientList.size() == 0){
            throw new NoResultFoundException();
        }else{
            return new ResponseEntity<>(clientList, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<List<Client>> getAllClient(){
        List<Client> clientList = this.clientRepository.findAll();

        if(clientList.size() == 0){
            throw new DatabaseIsEmptyException();
        }else{
            return new ResponseEntity<>(clientList, HttpStatus.OK);
        }
    }
}
