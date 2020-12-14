package com.mikason.PropView.services.commercialService;

import com.mikason.PropView.dataaccess.commercialEntity.ClientCriteria;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ClientCriteriaService {
    ResponseEntity<ClientCriteria> saveClientCriteria(ClientCriteria clientCriteria);
    ResponseEntity<ClientCriteria> editClientCriteria(ClientCriteria oldClientCriteria, ClientCriteria newClientCriteria);
    ResponseEntity<ClientCriteria> deleteClientCriteria(ClientCriteria clientCriteria);
    ResponseEntity<ClientCriteria> deleteAllClientCriteria();
    ResponseEntity<List<ClientCriteria>> searchClientCriteria(ClientCriteria clientCriteria);
    ResponseEntity<List<ClientCriteria>> getAllClientCriteria();
}
