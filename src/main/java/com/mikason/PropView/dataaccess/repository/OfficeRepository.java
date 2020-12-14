package com.mikason.PropView.dataaccess.repository;

import com.mikason.PropView.dataaccess.estateEntity.Office;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfficeRepository extends JpaRepository<Office, Long> {
}
