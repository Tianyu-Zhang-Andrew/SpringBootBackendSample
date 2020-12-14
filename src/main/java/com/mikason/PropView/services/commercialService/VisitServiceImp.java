package com.mikason.PropView.services.commercialService;

import com.google.gson.Gson;
import com.mikason.PropView.Exception.commercialEntityException.*;
import com.mikason.PropView.Exception.databaseException.DatabaseIsEmptyException;
import com.mikason.PropView.Exception.databaseException.NoResultFoundException;
import com.mikason.PropView.Exception.estateEntityException.PropertyNotFoundException;
import com.mikason.PropView.Exception.peopleEntityException.ClientNotFoundException;
import com.mikason.PropView.dataaccess.commercialEntity.Agent;
import com.mikason.PropView.dataaccess.commercialEntity.Duration;
import com.mikason.PropView.dataaccess.commercialEntity.Visit;
import com.mikason.PropView.dataaccess.estateEntity.Property;
import com.mikason.PropView.dataaccess.peopleEntity.Client;
import com.mikason.PropView.dataaccess.repository.AgentRepository;
import com.mikason.PropView.dataaccess.repository.ClientRepository;
import com.mikason.PropView.dataaccess.repository.PropertyRepository;
import com.mikason.PropView.dataaccess.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class VisitServiceImp implements VisitService{

    @Autowired
    private VisitRepository visitRespository;
    @Autowired
    private AgentRepository agentRespository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private PropertyRepository propertyRepository;

    @Override
    public ResponseEntity<Visit> saveVisit(Visit visit) {

        Optional<Agent> existAgent = this.agentRespository.findById(visit.getAgent().getId());
        Optional<Client> existClient = this.clientRepository.findById(visit.getClient().getId());
        Optional<Property> existProperty = this.propertyRepository.findById(visit.getProperty().getId());

        if(existAgent.isPresent() && existClient.isPresent() && existProperty.isPresent()){
            Example<Visit> visitExample = Example.of(visit);
            Optional<Visit> existVisit = this.visitRespository.findOne(visitExample);

            if(existVisit.isPresent()){
                throw new VisitAlreadyExistException();

            }else{
                existClient.get().addVisit(visit);
                existAgent.get().addVisit(visit);
                existProperty.get().addVisit(visit);

                this.propertyRepository.save(existProperty.get());
                this.agentRespository.save(existAgent.get());
                this.clientRepository.save(existClient.get());

                Optional<Visit> savedVisit = this.visitRespository.findOne(visitExample);

                return new ResponseEntity<>(savedVisit.get(), HttpStatus.CREATED);
            }

        }else if(!existAgent.isPresent()){
            throw new AgentNotFoundException(visit.getAgent().getId());

        }else if(!existClient.isPresent()){
            throw new ClientNotFoundException(visit.getClient().getId());

        }else{
            throw new PropertyNotFoundException(visit.getProperty().getId());
        }
    }

    @Override
    public ResponseEntity<Visit> editVisit(Visit visit) {
        Optional<Visit> existVisit = this.visitRespository.findById(visit.getId());

        if(!existVisit.isPresent()){
            throw new VisitNotFoundException();

        }else{
            Optional<Client> existClientOp = this.clientRepository.findById(visit.getClient().getId());
            Optional<Agent> existAgentOp = this.agentRespository.findById(visit.getAgent().getId());
            Optional<Property> existPropertyOp = this.propertyRepository.findById(visit.getProperty().getId());

            if(existAgentOp.isPresent() && existClientOp.isPresent() && existPropertyOp.isPresent()){

                Client existClient = existClientOp.get();
                Agent existAgent = existAgentOp.get();
                Property existProperty = existPropertyOp.get();

                Optional<Visit> visitToDeleteOp = this.visitRespository.findById(visit.getId());

                if(visitToDeleteOp.isPresent()) {
                    Visit visitToDelete = visitToDeleteOp.get();

                    Optional<Client> pastClientOp = this.clientRepository.findById(visitToDelete.getClient().getId());
                    Optional<Agent> pastAgentOp = this.agentRespository.findById(visitToDelete.getAgent().getId());
                    Optional<Property> pastPropertyOp = this.propertyRepository.findById(visitToDelete.getProperty().getId());

                    Client pastClient = pastClientOp.get();
                    Agent pastAgent = pastAgentOp.get();
                    Property pastProperty = pastPropertyOp.get();

                    pastClient.deleteVisit(visitToDelete);
                    pastAgent.deleteVisit(visitToDelete);
                    pastProperty.deleteVisit(visitToDelete);

                    this.clientRepository.save(pastClient);
                    this.agentRespository.save(pastAgent);
                    this.propertyRepository.save(pastProperty);

                    existClient.addVisit(visit);
                    existAgent.addVisit(visit);
                    existProperty.addVisit(visit);

                    this.propertyRepository.save(existProperty);
                    this.clientRepository.save(existClient);
                    this.agentRespository.save(existAgent);


                    Example<Visit> visitExample = Example.of(visit);
                    Optional<Visit> savedVisit = this.visitRespository.findOne(visitExample);

                    return new ResponseEntity<>(savedVisit.get(), HttpStatus.CREATED);
                }else{
                    throw new VisitNotFoundException();
                }

            }else if(!existAgentOp.isPresent()){
                throw new AgentNotFoundException(visit.getAgent().getId());

            }else if(!existClientOp.isPresent()){
                throw new ClientNotFoundException(visit.getClient().getId());

            }else{
                throw new PropertyNotFoundException(visit.getProperty().getId());
            }
        }
    }

    @Override
    public ResponseEntity<Visit> deleteVisit(Visit visit) {

        Optional<Visit> existVisit = this.visitRespository.findById(visit.getId());

        if(!existVisit.isPresent()){
            throw new VisitNotFoundException();

        }else{
            this.visitRespository.deleteById(visit.getId());

            Optional<Client> pastClientOp = this.clientRepository.findById(visit.getClient().getId());
            Optional<Agent> pastAgentOp = this.agentRespository.findById(visit.getAgent().getId());
            Optional<Property> pastPropertyOp = this.propertyRepository.findById(visit.getProperty().getId());

            Client pastClient = pastClientOp.get();
            Agent pastAgent = pastAgentOp.get();
            Property pastProperty = pastPropertyOp.get();

            pastClient.deleteVisit(existVisit.get());
            pastAgent.deleteVisit(existVisit.get());
            pastProperty.deleteVisit(existVisit.get());

            this.clientRepository.save(pastClient);
            this.agentRespository.save(pastAgent);
            this.propertyRepository.save(pastProperty);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    @Override
    public ResponseEntity<List<Visit>> searchVisit(Visit visit, Date start, Date end) {

        //Search all visit between a period
        if (visit == null) {
            List<Visit> allVisitList = this.visitRespository.findAll();
            if (start != null && end != null) {
                List<Visit> resultList = new ArrayList<>();
                for (Visit searchedVisit : allVisitList) {
                    String durationStr = searchedVisit.getDuration();
                    Gson gson = new Gson();
                    Duration duration = gson.fromJson(durationStr, Duration.class);

                    Date startTime = duration.getStartTime();
                    Date endTime = duration.getEndTime();

                    if (!(startTime.after(end) || endTime.before(start))) {
                        resultList.add(searchedVisit);
                    }
                }
                return new ResponseEntity<>(resultList, HttpStatus.OK);

            }else{
                throw new WrongVisitSearchException();
            }

            //Search all visit in a period by some restriction
        } else {
            Example<Visit> visitExample = Example.of(visit);
            List<Visit> visitList = this.visitRespository.findAll(visitExample);

            if (visitList.size() == 0) {
                throw new NoResultFoundException();
            } else {

                if (start != null && end != null) {
                    List<Visit> resultList = new ArrayList<>();
                    for (Visit searchedVisit : visitList) {
                        String durationStr = searchedVisit.getDuration();
                        Gson gson = new Gson();
                        Duration duration = gson.fromJson(durationStr, Duration.class);

                        Date startTime = duration.getStartTime();
                        Date endTime = duration.getEndTime();

                        if (!(startTime.after(end) || endTime.before(start))) {
                            resultList.add(searchedVisit);
                        }
                    }
                    return new ResponseEntity<>(resultList, HttpStatus.OK);

                    //Search visit regardless of time
                } else if (start == null && end == null) {
                    return new ResponseEntity<>(visitList, HttpStatus.OK);

                    //Only one of star and end is not null, wrong request format
                } else {
                    throw new WrongDurationRequestException();
                }
            }
        }
    }

    @Override
    public ResponseEntity<Visit> deleteAllVisit() {
        List<Visit> visitList = this.visitRespository.findAll();
        for(Visit vis : visitList){
            deleteVisit(vis);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<Visit>> getAllVisit() {
        List<Visit> visitList = this.visitRespository.findAll();

        if(visitList.size() == 0){
            throw new DatabaseIsEmptyException();
        }else{
            return new ResponseEntity<>(visitList, HttpStatus.OK);
        }
    }
}
