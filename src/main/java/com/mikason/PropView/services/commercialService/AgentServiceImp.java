package com.mikason.PropView.services.commercialService;

import com.mikason.PropView.Exception.commercialEntityException.AgentAlreadyExistException;
import com.mikason.PropView.Exception.commercialEntityException.AgentNotFoundException;
import com.mikason.PropView.Exception.estateEntityException.OfficeNotFoundException;
import com.mikason.PropView.Exception.databaseException.DatabaseIsEmptyException;
import com.mikason.PropView.Exception.databaseException.NoResultFoundException;
import com.mikason.PropView.dataaccess.commercialEntity.Agent;
import com.mikason.PropView.dataaccess.estateEntity.Office;
import com.mikason.PropView.dataaccess.repository.AgentRepository;
import com.mikason.PropView.dataaccess.repository.OfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AgentServiceImp implements AgentService{

    @Autowired
    private AgentRepository agentRespository;
    @Autowired
    private OfficeRepository officeRespository;


    @Override
    public ResponseEntity<Agent> saveAgent(Agent agent){
        Optional<Office> existOffice= this.officeRespository.findById(agent.getOffice().getId());

        if(existOffice.isPresent()) {
            Example<Agent> agentExample = Example.of(agent);
            Optional<Agent> existAgent = this.agentRespository.findOne(agentExample);
            if (existAgent.isPresent()) {
                throw new AgentAlreadyExistException();

            } else {
                existOffice.get().getAgents().add(agent);
                this.officeRespository.save(existOffice.get());
                Optional<Agent> savedAgent = this.agentRespository.findOne(agentExample);

                return new ResponseEntity<>(savedAgent.get(), HttpStatus.CREATED);
            }

        }else{
            throw new OfficeNotFoundException(agent.getOffice().getId());
        }
    }

    @Override
    public ResponseEntity<Agent> editAgent(Agent agent) {
        Optional<Office> existOffice= this.officeRespository.findById(agent.getOffice().getId());

        if(existOffice.isPresent()) {
            Optional<Agent> existAgent = this.agentRespository.findById(agent.getId());

            if (!existAgent.isPresent()) {
                throw new AgentNotFoundException(agent.getId());
            } else {
                //Remove old agent from office
                Optional<Office> oldOffice = this.officeRespository.findById(existAgent.get().getOffice().getId());
                oldOffice.get().getAgents().remove(existAgent.get());

                //Add new agent to office
                existOffice.get().getAgents().add(agent);

                this.officeRespository.save(oldOffice.get());
                this.officeRespository.save(existOffice.get());

                Example<Agent> agentExample = Example.of(agent);
                Optional<Agent> savedAgent = this.agentRespository.findOne(agentExample);

                return new ResponseEntity<>(savedAgent.get(), HttpStatus.CREATED);
            }

        }else{
            throw new OfficeNotFoundException(agent.getOffice().getId());
        }
    }

    @Override
    public ResponseEntity<Agent> deleteAgent(Agent agent) {
        Optional<Agent> existAgent = this.agentRespository.findById(agent.getId());

        if(!existAgent.isPresent()) {
            throw new AgentNotFoundException(agent.getId());
        }else {
            Optional<Office> existOffice = this.officeRespository.findById(existAgent.get().getOffice().getId());
            existOffice.get().getAgents().remove(existAgent.get());
            this.officeRespository.save(existOffice.get());
            this.agentRespository.delete(agent);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @Override
    public ResponseEntity<Agent> deleteAllAgent(){
        this.agentRespository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<Agent>> searchAgent(Agent agent){
        Example<Agent> agentExample =  Example.of(agent);
        List<Agent> agentList = this.agentRespository.findAll(agentExample);

        if(agentList.size() == 0){
            throw new NoResultFoundException();
        }else{
            return new ResponseEntity<>(agentList, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<List<Agent>> getAllAgent(){
        List<Agent> agentList = this.agentRespository.findAll();

        if(agentList.size() == 0){
            throw new DatabaseIsEmptyException();
        }else{
            return new ResponseEntity<>(agentList, HttpStatus.OK);
        }
    }
}
