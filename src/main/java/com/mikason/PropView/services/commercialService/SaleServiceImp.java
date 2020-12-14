package com.mikason.PropView.services.commercialService;

import com.mikason.PropView.Exception.commercialEntityException.AgentNotFoundException;
import com.mikason.PropView.Exception.commercialEntityException.PropertySaleNotFoundException;
import com.mikason.PropView.Exception.commercialEntityException.SaleAlreadyExistException;
import com.mikason.PropView.Exception.commercialEntityException.SaleNotFoundException;
import com.mikason.PropView.Exception.databaseException.DatabaseIsEmptyException;
import com.mikason.PropView.Exception.databaseException.NoResultFoundException;
import com.mikason.PropView.Exception.peopleEntityException.ClientNotFoundException;
import com.mikason.PropView.dataaccess.commercialEntity.Agent;
import com.mikason.PropView.dataaccess.commercialEntity.PropertySale;
import com.mikason.PropView.dataaccess.commercialEntity.Sale;
import com.mikason.PropView.dataaccess.peopleEntity.Client;
import com.mikason.PropView.dataaccess.repository.AgentRepository;
import com.mikason.PropView.dataaccess.repository.ClientRepository;
import com.mikason.PropView.dataaccess.repository.PropertySaleRepository;
import com.mikason.PropView.dataaccess.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SaleServiceImp implements SaleService{
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private PropertySaleRepository propertySaleRepository;
    @Autowired
    private PropertySaleService propertySaleService;

    @Override
    public ResponseEntity<Sale> saveSale(Sale sale) {
        Optional<Agent> existAgent = this.agentRepository.findById(sale.getSaleKey().getAgent().getId());
        Optional<Client> existClient = this.clientRepository.findById(sale.getSaleKey().getClient().getId());
        Optional<PropertySale> existPropertySale = this.propertySaleRepository.findById(sale.getSaleKey().getPropertySale().getId());

        if(existAgent.isPresent() && existClient.isPresent() && existPropertySale.isPresent()){
            Optional<Sale> existSale = this.saleRepository.findById(sale.getSaleKey());

            if(existSale.isPresent()){
                throw new SaleAlreadyExistException();
            }else{
                existPropertySale.get().getSales().add(sale);
                existAgent.get().getSales().add(sale);
                existClient.get().getSales().add(sale);

                propertySaleService.editPropertySale(existPropertySale.get());
                this.agentRepository.save(existAgent.get()).getSales();
                this.clientRepository.save(existClient.get()).getSales();

                Optional<Sale> savedSale = this.saleRepository.findById(sale.getSaleKey());
                return new ResponseEntity<>(savedSale.get(), HttpStatus.CREATED);
            }

        }else if(!existAgent.isPresent()){
            throw new AgentNotFoundException(existAgent.get().getId());
        }else if(!existClient.isPresent()){
            throw new ClientNotFoundException(existClient.get().getId());
        }else {
            throw new PropertySaleNotFoundException(existPropertySale.get().getId());
        }
    }

    @Override
    public ResponseEntity<Sale> editSale(Sale oldSale ,Sale newSale) {
        Optional<Sale> oldSaleOpt = this.saleRepository.findById(oldSale.getSaleKey());

        if(oldSaleOpt.isPresent()){
            Optional<Agent> newAgent = this.agentRepository.findById(newSale.getSaleKey().getAgent().getId());
            Optional<Client> newClient = this.clientRepository.findById(newSale.getSaleKey().getClient().getId());
            Optional<PropertySale> newPropertySale = this.propertySaleRepository.findById(newSale.getSaleKey().getPropertySale().getId());

            if(newAgent.isPresent() && newClient.isPresent() && newPropertySale.isPresent()){
                Optional<Agent> oldAgent = this.agentRepository.findById(oldSale.getSaleKey().getAgent().getId());
                Optional<Client> oldClient = this.clientRepository.findById(oldSale.getSaleKey().getClient().getId());
                Optional<PropertySale> oldPropertySale = this.propertySaleRepository.findById(oldSale.getSaleKey().getPropertySale().getId());

                //Edit old propertySale, client and agent, delete old sale
                oldAgent.get().deleteSale(oldSaleOpt.get());
                oldClient.get().deleteSale(oldSaleOpt.get());
                oldPropertySale.get().deleteSale(oldSaleOpt.get());

                this.agentRepository.save(oldAgent.get());
                this.clientRepository.save(oldClient.get());
                this.propertySaleService.editPropertySale(oldPropertySale.get());
                this.saleRepository.deleteById(oldSale.getSaleKey());

                //Edit new propertySale, agent and client, save new sale
                newAgent.get().addSale(newSale);
                newClient.get().addSale(newSale);
                newPropertySale.get().addSale(newSale);

                this.propertySaleService.editPropertySale(newPropertySale.get());
                this.agentRepository.save(newAgent.get());
                this.clientRepository.save(newClient.get());

                Optional<Sale> savedSale = this.saleRepository.findById(newSale.getSaleKey());
                return new ResponseEntity<>(savedSale.get(), HttpStatus.CREATED);

            }else if(!newAgent.isPresent()){
                throw new AgentNotFoundException(newAgent.get().getId());
            }else if(!newClient.isPresent()){
                throw new ClientNotFoundException(newClient.get().getId());
            }else {
                throw new PropertySaleNotFoundException(newPropertySale.get().getId());
            }

        }else{
            throw new SaleNotFoundException();
        }
    }

    @Override
    public ResponseEntity<Sale> deleteSale(Sale sale) {
        Optional<Agent> existAgent = this.agentRepository.findById(sale.getSaleKey().getAgent().getId());
        Optional<Client> existClient = this.clientRepository.findById(sale.getSaleKey().getClient().getId());
        Optional<PropertySale> existPropertySale = this.propertySaleRepository.findById(sale.getSaleKey().getPropertySale().getId());

        if(existAgent.isPresent() && existClient.isPresent() && existPropertySale.isPresent()){
            Optional<Sale> existSale = this.saleRepository.findById(sale.getSaleKey());

            if(existSale.isPresent()){
                this.saleRepository.deleteById(sale.getSaleKey());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }else{
                throw new SaleNotFoundException();
            }

        }else if(!existAgent.isPresent()){
            throw new AgentNotFoundException(existAgent.get().getId());
        }else if(!existClient.isPresent()){
            throw new ClientNotFoundException(existClient.get().getId());
        }else {
            throw new PropertySaleNotFoundException(existPropertySale.get().getId());
        }
    }

    @Override
    public ResponseEntity<Sale> deleteAllSale(){
        List<Sale> saleList = this.saleRepository.findAll();

        for(Sale sale : saleList){
            deleteSale(sale);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<Sale>> searchSale(Sale sale) {
        Example<Sale> saleExample =  Example.of(sale);
        List<Sale> propList = this.saleRepository.findAll(saleExample);

        if(propList.size() == 0){
            throw new NoResultFoundException();
        }else {
            return new ResponseEntity<>(propList, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<List<Sale>> getAllSale() {
        List<Sale> salesList = this.saleRepository.findAll();

        if(salesList.size() == 0){
            throw new DatabaseIsEmptyException();
        }else{
            return new ResponseEntity<>(salesList, HttpStatus.OK);
        }
    }
}
