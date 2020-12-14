package com.mikason.PropView.dataaccess.repository;

import com.mikason.PropView.dataaccess.documentEntity.SaleDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleDocumentRepository extends JpaRepository<SaleDocument, Long> {
}
