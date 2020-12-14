package com.mikason.PropView.controller;

import com.google.gson.Gson;
import com.mikason.PropView.dataaccess.commercialEntity.Sale;
import com.mikason.PropView.services.commercialService.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SaleController {

    @Autowired
    private SaleService saleService;

    @PostMapping("/sale/saveSale")
    ResponseEntity<Sale> saveSale(@RequestBody Sale sale){
        return saleService.saveSale(sale);
    }

    @PostMapping("/sale/editSale")
    ResponseEntity<Sale> editSale(@RequestParam(value = "oldSale", required=false) String oldSaleStr,
                                  @RequestParam(value = "newSale",required=false) String newSaleStr){

        Gson gson = new Gson();
        Sale oldSale = gson.fromJson(oldSaleStr, Sale.class);
        Sale newSale = gson.fromJson(newSaleStr, Sale.class);

        return saleService.editSale(oldSale, newSale);
    }

    @PostMapping("/sale/deleteSale")
    ResponseEntity<Sale> deleteSale(@RequestBody Sale sale){
        return saleService.deleteSale(sale);
    }

    @PostMapping("/sale/deleteAllSale")
    ResponseEntity<Sale> deleteAllSale(){
        return saleService.deleteAllSale();
    }

    @PostMapping("/sale/searchSale")
    ResponseEntity<List<Sale>> searchSale(@RequestBody Sale sale){
        return saleService.searchSale(sale);
    }

    @GetMapping("/sale/getAllSale")
    ResponseEntity<List<Sale>> getAllSale(){
        return saleService.getAllSale();
    }
}
