package com.mikason.PropView.services.commercialService;

import com.mikason.PropView.Exception.commercialEntityException.ClientCriteriaAlreadyExistException;
import com.mikason.PropView.Exception.commercialEntityException.ClientCriteriaNotFoundCriteria;
import com.mikason.PropView.Exception.commercialEntityException.FeatureNotFoundException;
import com.mikason.PropView.Exception.databaseException.DatabaseIsEmptyException;
import com.mikason.PropView.Exception.databaseException.NoResultFoundException;
import com.mikason.PropView.Exception.peopleEntityException.ClientNotFoundException;
import com.mikason.PropView.dataaccess.commercialEntity.ClientCriteria;
import com.mikason.PropView.dataaccess.commercialEntity.Feature;
import com.mikason.PropView.dataaccess.peopleEntity.Client;
import com.mikason.PropView.dataaccess.repository.ClientCriteriaRepository;
import com.mikason.PropView.dataaccess.repository.ClientRepository;
import com.mikason.PropView.dataaccess.repository.FeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientCriteriaImp implements ClientCriteriaService{

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private FeatureRepository featureRepository;
    @Autowired
    private ClientCriteriaRepository clientCriteriaRepository;

    @Override
    public ResponseEntity<ClientCriteria> saveClientCriteria(ClientCriteria clientCriteria) {
        Optional<Client> existClient = this.clientRepository.findById(clientCriteria.getClientCriteriaKey().getClient().getId());
        Optional<Feature> existFeature = this.featureRepository.findById(clientCriteria.getClientCriteriaKey().getFeature().getId());

        if(existClient.isPresent() && existFeature.isPresent()){
            Optional<ClientCriteria> existClientCriteriaOpt = this.clientCriteriaRepository.findById(clientCriteria.getClientCriteriaKey());
            if(existClientCriteriaOpt.isPresent()){
                throw new ClientCriteriaAlreadyExistException();
            }else{
                ClientCriteria savedClientCriteria = this.clientCriteriaRepository.save(clientCriteria);
                return new ResponseEntity<>(savedClientCriteria, HttpStatus.CREATED);
            }

        }else if(!existClient.isPresent()){
            throw new ClientNotFoundException(existClient.get().getId());
        }else{
            throw new FeatureNotFoundException(existFeature.get().getId());
        }
    }

    @Override
    public ResponseEntity<ClientCriteria> editClientCriteria(ClientCriteria oldClientCriteria, ClientCriteria newClientCriteria) {
        Optional<Client> oldExistClient = this.clientRepository.findById(oldClientCriteria.getClientCriteriaKey().getClient().getId());
        Optional<Feature> oldExistFeature = this.featureRepository.findById(oldClientCriteria.getClientCriteriaKey().getFeature().getId());

        if(oldExistClient.isPresent() && oldExistFeature.isPresent()){
            Optional<Client> newExistClient = this.clientRepository.findById(newClientCriteria.getClientCriteriaKey().getClient().getId());
            Optional<Feature> newExistFeature = this.featureRepository.findById(newClientCriteria.getClientCriteriaKey().getFeature().getId());

            if(newExistClient.isPresent() && newExistFeature.isPresent()){

                Optional<ClientCriteria> oldClientCriteriaOpt = this.clientCriteriaRepository.findById(oldClientCriteria.getClientCriteriaKey());
                Optional<ClientCriteria> newClientCriteriaOpt = this.clientCriteriaRepository.findById(newClientCriteria.getClientCriteriaKey());

                if(newClientCriteriaOpt.isPresent()){
                    throw new ClientCriteriaAlreadyExistException();
                }else if(!oldClientCriteriaOpt.isPresent()){
                    throw new ClientCriteriaNotFoundCriteria();
                }else{
                    this.clientCriteriaRepository.deleteById(oldClientCriteria.getClientCriteriaKey());
                    ClientCriteria savedClientCriteria = this.clientCriteriaRepository.save(newClientCriteria);
                    return new ResponseEntity<>(savedClientCriteria, HttpStatus.CREATED);
                }

            }else if(!newExistClient.isPresent()){
                throw new ClientNotFoundException(newExistClient.get().getId());
            }else{
                throw new FeatureNotFoundException(newExistFeature.get().getId());
            }

        }else if(!oldExistClient.isPresent()){
            throw new ClientNotFoundException(oldExistClient.get().getId());
        }else{
            throw new FeatureNotFoundException(oldExistFeature.get().getId());
        }
    }

    @Override
    public ResponseEntity<ClientCriteria> deleteClientCriteria(ClientCriteria clientCriteria) {
        Optional<Client> existClient = this.clientRepository.findById(clientCriteria.getClientCriteriaKey().getClient().getId());
        Optional<Feature> existFeature = this.featureRepository.findById(clientCriteria.getClientCriteriaKey().getFeature().getId());

        if(existClient.isPresent() && existFeature.isPresent()){
            Optional<ClientCriteria> existOwnerClientCriteriaOpt = this.clientCriteriaRepository.findById(clientCriteria.getClientCriteriaKey());

            if(existOwnerClientCriteriaOpt.isPresent()) {
                this.clientCriteriaRepository.deleteById(clientCriteria.getClientCriteriaKey());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            }else{
                throw new ClientCriteriaNotFoundCriteria();
            }

        }else if(!existClient.isPresent()){
            throw new ClientNotFoundException(existClient.get().getId());
        }else{
            throw new FeatureNotFoundException(existFeature.get().getId());
        }
    }

    @Override
    public ResponseEntity<ClientCriteria> deleteAllClientCriteria() {
        List<ClientCriteria> ownerClientCriteriaList = this.clientCriteriaRepository.findAll();

        for(ClientCriteria savedClientCriteriaProperty : ownerClientCriteriaList){
            deleteClientCriteria(savedClientCriteriaProperty);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<ClientCriteria>> searchClientCriteria(ClientCriteria clientCriteria) {
        Example<ClientCriteria> clientCriteriaExample =  Example.of(clientCriteria);
        List<ClientCriteria> clientCriteriaList = this.clientCriteriaRepository.findAll(clientCriteriaExample);

        if(clientCriteriaList.size() == 0){
            throw new NoResultFoundException();
        }else {
            return new ResponseEntity<>(clientCriteriaList, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<List<ClientCriteria>> getAllClientCriteria() {
        List<ClientCriteria> ownerClientCriteriaList = this.clientCriteriaRepository.findAll();

        if(ownerClientCriteriaList.size() == 0){
            throw new DatabaseIsEmptyException();
        }else{
            return new ResponseEntity<>(ownerClientCriteriaList, HttpStatus.OK);
        }
    }
}
