package com.mikason.PropView.dataaccess.repository;

import com.mikason.PropView.dataaccess.documentEntity.RentalAgreement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalAgreementRepository extends JpaRepository<RentalAgreement, Long> {
}
