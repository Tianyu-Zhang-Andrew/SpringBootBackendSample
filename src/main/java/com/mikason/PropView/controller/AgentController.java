package com.mikason.PropView.controller;

import com.mikason.PropView.dataaccess.commercialEntity.Agent;
import com.mikason.PropView.services.commercialService.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AgentController {

    @Autowired
    private AgentService agentService;

    @PostMapping("/agent/saveAgent")
    ResponseEntity<Agent> saveAgent(@RequestBody Agent agent){
        return agentService.saveAgent(agent);
    }

    @PostMapping("/agent/editAgent")
    ResponseEntity<Agent> editAgent(@RequestBody Agent agent){
        return agentService.editAgent(agent);
    }

    @PostMapping("/agent/deleteAgent")
    ResponseEntity<Agent> deleteAgent(@RequestBody Agent agent){
        return agentService.deleteAgent(agent);
    }

    @PostMapping("/agent/deleteAllAgent")
    ResponseEntity<Agent> deleteAllAgent(){
        return agentService.deleteAllAgent();
    }

    @PostMapping("/agent/searchAgent")
    ResponseEntity<List<Agent>> searchAgent(@RequestBody Agent agent){
        return agentService.searchAgent(agent);
    }

    @GetMapping("/agent/getAllAgent")
    ResponseEntity<List<Agent>> getAllAgent(){
        return agentService.getAllAgent();
    }
}
