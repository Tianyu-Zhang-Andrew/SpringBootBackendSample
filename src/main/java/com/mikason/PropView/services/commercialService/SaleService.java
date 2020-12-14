package com.mikason.PropView.services.commercialService;

import com.mikason.PropView.dataaccess.commercialEntity.Sale;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SaleService {
    ResponseEntity<Sale> saveSale(Sale sale);
    ResponseEntity<Sale> editSale(Sale oldSale, Sale newSale);
    ResponseEntity<Sale> deleteSale(Sale sale);
    ResponseEntity<Sale> deleteAllSale();
    ResponseEntity<List<Sale>> searchSale(Sale sale);
    ResponseEntity<List<Sale>> getAllSale();
}
