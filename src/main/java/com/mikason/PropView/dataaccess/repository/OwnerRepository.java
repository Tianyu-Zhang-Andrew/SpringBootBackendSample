package com.mikason.PropView.dataaccess.repository;

import com.mikason.PropView.dataaccess.peopleEntity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
}
