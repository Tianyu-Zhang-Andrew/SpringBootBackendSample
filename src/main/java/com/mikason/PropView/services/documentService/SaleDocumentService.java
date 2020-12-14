package com.mikason.PropView.services.documentService;

import com.mikason.PropView.dataaccess.documentEntity.SaleDocument;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SaleDocumentService {
    ResponseEntity<SaleDocument> saveSaleDocument(SaleDocument saleDocument);
    ResponseEntity<SaleDocument> editSaleDocument(SaleDocument saleDocument);
    ResponseEntity<List<SaleDocument>> searchSaleDocument(SaleDocument saleDocument);
    ResponseEntity<SaleDocument> deleteSaleDocument(SaleDocument saleDocument);
    ResponseEntity<SaleDocument> deleteAllSaleDocument();
    ResponseEntity<List<SaleDocument>> getAllSaleDocument();
}
