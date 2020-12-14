package com.mikason.PropView.services.peopleService;

import com.mikason.PropView.dataaccess.peopleEntity.Owner;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OwnerService {
    ResponseEntity<Owner> saveOwner(Owner owner);
    ResponseEntity<Owner> editOwner(Owner owner);
    ResponseEntity<Owner> deleteOwner(Owner owner);
    ResponseEntity<Owner> deleteAllOwner();
    ResponseEntity<List<Owner>> searchOwner(Owner owner);
    ResponseEntity<List<Owner>> getAllOwner();
}
