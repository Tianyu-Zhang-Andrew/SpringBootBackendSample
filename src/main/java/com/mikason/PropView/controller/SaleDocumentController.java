package com.mikason.PropView.controller;

import com.mikason.PropView.dataaccess.documentEntity.SaleDocument;
import com.mikason.PropView.services.documentService.SaleDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SaleDocumentController {
    @Autowired
    private SaleDocumentService saleDocumentService;

    @PostMapping("/saleDocument/saveSaleDocument")
    ResponseEntity<SaleDocument> saveSaleDocumentService(@RequestBody SaleDocument saleDocument){
        return saleDocumentService.saveSaleDocument(saleDocument);
    }

    @PostMapping("/saleDocument/editSaleDocument")
    ResponseEntity<SaleDocument> editRentalAgreement(@RequestBody SaleDocument saleDocument){
        return saleDocumentService.editSaleDocument(saleDocument);
    }

    @PostMapping("/saleDocument/searchSaleDocument")
    ResponseEntity<List<SaleDocument>> searchSaleDocument(@RequestBody SaleDocument saleDocument){
        return saleDocumentService.searchSaleDocument(saleDocument);
    }

    @PostMapping("/saleDocument/deleteSaleDocument")
    ResponseEntity<SaleDocument> deleteSaleDocument(@RequestBody SaleDocument saleDocument){
        return saleDocumentService.deleteSaleDocument(saleDocument);
    }

    @PostMapping("/saleDocument/deleteAllSaleDocument")
    ResponseEntity<SaleDocument> deleteAllSaleDocument(){
        return saleDocumentService.deleteAllSaleDocument();
    }

    @GetMapping("/saleDocument/getAllSaleDocument")
    ResponseEntity<List<SaleDocument>> getAllSaleDocument(){
        return saleDocumentService.getAllSaleDocument();
    }
}
