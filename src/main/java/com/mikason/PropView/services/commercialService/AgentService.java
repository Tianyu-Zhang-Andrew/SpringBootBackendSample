package com.mikason.PropView.services.commercialService;

import com.mikason.PropView.dataaccess.commercialEntity.Agent;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AgentService {
    ResponseEntity<Agent> saveAgent(Agent agent);
    ResponseEntity<Agent> editAgent(Agent agent);
    ResponseEntity<Agent> deleteAgent(Agent agent);
    ResponseEntity<Agent> deleteAllAgent();
    ResponseEntity<List<Agent>> searchAgent(Agent agent);
    ResponseEntity<List<Agent>> getAllAgent();
}
