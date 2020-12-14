package com.mikason.PropView.dataaccess.repository;

import com.mikason.PropView.dataaccess.estateEntity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
