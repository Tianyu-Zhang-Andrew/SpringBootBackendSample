package com.mikason.PropView.dataaccess.repository;

import com.mikason.PropView.dataaccess.commercialEntity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentRepository extends JpaRepository<Agent, Long> {
}
