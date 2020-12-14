package com.mikason.PropView.services.documentService;

import com.mikason.PropView.Exception.databaseException.DatabaseIsEmptyException;
import com.mikason.PropView.Exception.databaseException.NoResultFoundException;
import com.mikason.PropView.Exception.documentEntityException.RentalAgreementNotFoundException;
import com.mikason.PropView.Exception.documentEntityException.SaleDocumentAlreadyExistException;
import com.mikason.PropView.Exception.documentEntityException.SaleDocumentNotFoundException;
import com.mikason.PropView.Exception.commercialEntityException.RentNotFoundException;
import com.mikason.PropView.Exception.commercialEntityException.SaleNotFoundException;
import com.mikason.PropView.dataaccess.commercialEntity.Sale;
import com.mikason.PropView.dataaccess.documentEntity.SaleDocument;
import com.mikason.PropView.dataaccess.repository.SaleDocumentRepository;
import com.mikason.PropView.dataaccess.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SaleDocumentServiceImp implements SaleDocumentService{
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private SaleDocumentRepository saleDocumentRepository;

    @Override
    public ResponseEntity<SaleDocument> saveSaleDocument(SaleDocument saleDocument) {
        Optional<Sale> existSaleOpt = this.saleRepository.findById(saleDocument.getSale().getSaleKey());

        if(existSaleOpt.isPresent()){
            List<SaleDocument> saleDocumentList = this.saleDocumentRepository.findAll();
            for(SaleDocument savedSaleDocument: saleDocumentList){
                if(savedSaleDocument.getContent().equals(saleDocument.getContent()) && savedSaleDocument.getSale().equals(saleDocument.getSale())){
                    throw new SaleDocumentAlreadyExistException();
                }
            }

            SaleDocument savedSaleDocument = this.saleDocumentRepository.save(saleDocument);
            return new ResponseEntity<>(savedSaleDocument, HttpStatus.CREATED);

        }else{
            throw new SaleDocumentNotFoundException(saleDocument.getId());
        }
    }

    @Override
    public ResponseEntity<SaleDocument> editSaleDocument(SaleDocument saleDocument) {
        Optional<SaleDocument> existSaleDocumentOpt = this.saleDocumentRepository.findById(saleDocument.getId());
        if(existSaleDocumentOpt.isPresent()){

            Optional<Sale> newSaleOpt = this.saleRepository.findById(saleDocument.getSale().getSaleKey());
            if(newSaleOpt.isPresent()) {
                Optional<Sale> oldSaleOpt = this.saleRepository.findById(existSaleDocumentOpt.get().getSale().getSaleKey());
                oldSaleOpt.get().deleteSaleDocument(existSaleDocumentOpt.get());
                this.saleRepository.save(oldSaleOpt.get());

                SaleDocument savedSaleDocument = this.saleDocumentRepository.save(saleDocument);
                return new ResponseEntity<>(savedSaleDocument, HttpStatus.CREATED);

            }else {
                throw new SaleNotFoundException();
            }

        }else{
            throw new SaleDocumentNotFoundException(saleDocument.getId());
        }
    }

    @Override
    public ResponseEntity<List<SaleDocument>> searchSaleDocument(SaleDocument saleDocument) {
        Example<SaleDocument> exampleSaleDocument =  Example.of(saleDocument);
        List<SaleDocument> saleDocumentList = this.saleDocumentRepository.findAll(exampleSaleDocument);
        System.out.println(saleDocumentList);
        if(saleDocumentList.size() == 0){
            throw new NoResultFoundException();
        }else {
            return new ResponseEntity<>(saleDocumentList, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<SaleDocument> deleteSaleDocument(SaleDocument saleDocument) {
        Optional<SaleDocument> existSaleDocumentOpt = this.saleDocumentRepository.findById(saleDocument.getId());
        if(existSaleDocumentOpt.isPresent()){
            Optional<Sale> existRentOpt = this.saleRepository.findById(saleDocument.getSale().getSaleKey());
            if(existRentOpt.isPresent()) {
                this.saleDocumentRepository.deleteById(saleDocument.getId());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            }else{
                throw new RentNotFoundException();
            }

        }else{
            throw new RentalAgreementNotFoundException(saleDocument.getId());
        }
    }

    @Override
    public ResponseEntity<SaleDocument> deleteAllSaleDocument() {
        List<SaleDocument> saleDocumentList = this.saleDocumentRepository.findAll();

        for(SaleDocument savedSaleDocument : saleDocumentList){
            deleteSaleDocument(savedSaleDocument);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<SaleDocument>> getAllSaleDocument() {
        List<SaleDocument> saleDocumentList = this.saleDocumentRepository.findAll();

        if(saleDocumentList.size() == 0){
            throw new DatabaseIsEmptyException();
        }else{
            return new ResponseEntity<>(saleDocumentList, HttpStatus.OK);
        }
    }
}
