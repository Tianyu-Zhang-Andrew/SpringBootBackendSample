package com.mikason.PropView.services.commercialService;

import com.mikason.PropView.Exception.commercialEntityException.*;
import com.mikason.PropView.Exception.databaseException.DatabaseIsEmptyException;
import com.mikason.PropView.Exception.peopleEntityException.ClientNotFoundException;
import com.mikason.PropView.dataaccess.commercialEntity.Agent;
import com.mikason.PropView.dataaccess.commercialEntity.PropertyRent;
import com.mikason.PropView.dataaccess.commercialEntity.Rent;
import com.mikason.PropView.dataaccess.peopleEntity.Client;
import com.mikason.PropView.dataaccess.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RentServiceImp implements RentService{

    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private PropertyRentRepository propertyRentRepository;
    @Autowired
    private RentRepository rentRepository;
    @Autowired
    private PropertyRentService propertyRentService;
    @Autowired
    private RentalAgreementRepository rentalAgreementRepository;

    @Override
    public ResponseEntity<Rent> saveRent(Rent rent) {
        Optional<Agent> existAgent = this.agentRepository.findById(rent.getRentKey().getAgent().getId());
        Optional<Client> existClient = this.clientRepository.findById(rent.getRentKey().getClient().getId());
        Optional<PropertyRent> existPropertyRent = this.propertyRentRepository.findById(rent.getRentKey().getPropertyRent().getId());

        if(existAgent.isPresent() && existClient.isPresent() && existPropertyRent.isPresent()){
            Optional<Rent> existRent = this.rentRepository.findById(rent.getRentKey());

            if(existRent.isPresent()){
                throw new RentAlreadyExistException();
            }else{
                existPropertyRent.get().getRents().add(rent);
                existAgent.get().getRents().add(rent);
                existClient.get().getRents().add(rent);

                this.rentRepository.save(rent);
                propertyRentService.editPropertyRent(existPropertyRent.get());
                this.agentRepository.save(existAgent.get()).getSales();
                this.clientRepository.save(existClient.get()).getSales();

                Optional<Rent> savedRent = this.rentRepository.findById(rent.getRentKey());
                return new ResponseEntity<>(savedRent.get(), HttpStatus.CREATED);
            }

        }else if(!existAgent.isPresent()){
            throw new AgentNotFoundException(existAgent.get().getId());
        }else if(!existClient.isPresent()){
            throw new ClientNotFoundException(existClient.get().getId());
        }else {
            throw new PropertyRentNotFoundException(existPropertyRent.get().getId());
        }
    }

    @Override
    public ResponseEntity<Rent> deleteRent(Rent rent) {
        Optional<Agent> existAgent = this.agentRepository.findById(rent.getRentKey().getAgent().getId());
        Optional<Client> existClient = this.clientRepository.findById(rent.getRentKey().getClient().getId());
        Optional<PropertyRent> existPropertyRent = this.propertyRentRepository.findById(rent.getRentKey().getPropertyRent().getId());

        if(existAgent.isPresent() && existClient.isPresent() && existPropertyRent.isPresent()){
            Optional<Rent> existRent = this.rentRepository.findById(rent.getRentKey());

            if(existRent.isPresent()){
                existAgent.get().deleteRent(rent);
                existClient.get().deleteRent(rent);
                existPropertyRent.get().deleteRent(rent);

                this.clientRepository.save(existClient.get());
                this.agentRepository.save(existAgent.get());
                this.propertyRentService.editPropertyRent(existPropertyRent.get());
                this.rentRepository.deleteById(rent.getRentKey());

                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }else{
                throw new SaleNotFoundException();
            }

        }else if(!existAgent.isPresent()){
            throw new AgentNotFoundException(existAgent.get().getId());
        }else if(!existClient.isPresent()){
            throw new ClientNotFoundException(existClient.get().getId());
        }else {
            throw new PropertyRentNotFoundException(existPropertyRent.get().getId());
        }
    }

    @Override
    public ResponseEntity<Rent> editRent(Rent oldRent, Rent newRent) {
        Optional<Rent> oldRentOpt = this.rentRepository.findById(oldRent.getRentKey());

        if(oldRentOpt.isPresent()){
            Optional<Agent> newAgent = this.agentRepository.findById(newRent.getRentKey().getAgent().getId());
            Optional<Client> newClient = this.clientRepository.findById(newRent.getRentKey().getClient().getId());
            Optional<PropertyRent> newPropertyRent = this.propertyRentRepository.findById(newRent.getRentKey().getPropertyRent().getId());

            if(newAgent.isPresent() && newClient.isPresent() && newPropertyRent.isPresent()){
                Optional<Agent> oldAgent = this.agentRepository.findById(oldRent.getRentKey().getAgent().getId());
                Optional<Client> oldClient = this.clientRepository.findById(oldRent.getRentKey().getClient().getId());
                Optional<PropertyRent> oldPropertyRent = this.propertyRentRepository.findById(oldRent.getRentKey().getPropertyRent().getId());

                //Edit old propertyRent, client and agent, delete old sale
                oldAgent.get().deleteRent(oldRentOpt.get());
                oldClient.get().deleteRent(oldRentOpt.get());
                oldPropertyRent.get().deleteRent(oldRentOpt.get());

                this.agentRepository.save(oldAgent.get());
                this.clientRepository.save(oldClient.get());
                this.propertyRentService.editPropertyRent(oldPropertyRent.get());
                this.rentRepository.deleteById(oldRent.getRentKey());

                //Edit new propertySale, agent and client, save new rent
                newAgent.get().addRent(newRent);
                newClient.get().addRent(newRent);
                newPropertyRent.get().addRent(newRent);

                this.rentRepository.save(newRent);
                this.propertyRentService.editPropertyRent(newPropertyRent.get());
                this.agentRepository.save(newAgent.get());
                this.clientRepository.save(newClient.get());

                Optional<Rent> savedSale = this.rentRepository.findById(newRent.getRentKey());
                return new ResponseEntity<>(savedSale.get(), HttpStatus.CREATED);

            }else if(!newAgent.isPresent()){
                throw new AgentNotFoundException(newAgent.get().getId());
            }else if(!newClient.isPresent()){
                throw new ClientNotFoundException(newClient.get().getId());
            }else {
                throw new PropertyRentNotFoundException(newPropertyRent.get().getId());
            }

        }else{
            throw new RentNotFoundException();
        }
    }

    @Override
    public ResponseEntity<Rent> deleteAllRent() {
        List<Rent> rentList = this.rentRepository.findAll();

        for(Rent rent : rentList){
            deleteRent(rent);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<Rent>> getAllRent() {
        List<Rent> rentsList = this.rentRepository.findAll();

        if(rentsList.size() == 0){
            throw new DatabaseIsEmptyException();
        }else{
            return new ResponseEntity<>(rentsList, HttpStatus.OK);
        }
    }
}
