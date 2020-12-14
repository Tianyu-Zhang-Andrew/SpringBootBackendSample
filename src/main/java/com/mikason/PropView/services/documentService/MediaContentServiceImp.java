package com.mikason.PropView.services.documentService;

import com.mikason.PropView.Exception.databaseException.DatabaseIsEmptyException;
import com.mikason.PropView.Exception.databaseException.NoResultFoundException;
import com.mikason.PropView.Exception.documentEntityException.MediaContentAlreadyExistException;
import com.mikason.PropView.Exception.documentEntityException.MediaContentNotFoundException;
import com.mikason.PropView.Exception.documentEntityException.MediaContentParameterException;
import com.mikason.PropView.Exception.commercialEntityException.PropertyRentNotFoundException;
import com.mikason.PropView.Exception.commercialEntityException.PropertySaleNotFoundException;
import com.mikason.PropView.dataaccess.commercialEntity.PropertyRent;
import com.mikason.PropView.dataaccess.commercialEntity.PropertySale;
import com.mikason.PropView.dataaccess.documentEntity.MediaContent;
import com.mikason.PropView.dataaccess.repository.MediaContentRepository;
import com.mikason.PropView.dataaccess.repository.PropertyRentRepository;
import com.mikason.PropView.dataaccess.repository.PropertySaleRepository;
import com.mikason.PropView.services.commercialService.PropertyRentService;
import com.mikason.PropView.services.commercialService.PropertySaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MediaContentServiceImp implements MediaContentService{
    @Autowired
    private MediaContentRepository mediaContentRepository;
    @Autowired
    private PropertyRentRepository propertyRentRepository;
    @Autowired
    private PropertySaleRepository propertySaleRepository;
    @Autowired
    private PropertyRentService propertyRentService;
    @Autowired
    private PropertySaleService propertySaleService;

    @Override
    public ResponseEntity<MediaContent> saveMediaContent(MediaContent mediaContent) {

        if(mediaContent.getPropertySale() == null && mediaContent.getPropertyRent() == null){
            throw new MediaContentParameterException();

        }else if(mediaContent.getPropertySale() != null && mediaContent.getPropertyRent() != null) {
            throw new MediaContentParameterException();

        }else{
            //MediaContent for propertySale
            if(mediaContent.getPropertySale() != null){
                Optional<PropertySale> existPropertySaleOpt = this.propertySaleRepository.findById(mediaContent.getPropertySale().getId());

                if(existPropertySaleOpt.isPresent()){
                    Example<MediaContent> mediaContentExample =  Example.of(mediaContent);
                    Optional<MediaContent> existMediaContent = this.mediaContentRepository.findOne(mediaContentExample);
                    if(existMediaContent.isPresent()){
                        throw new MediaContentAlreadyExistException();

                    }else{
                        existPropertySaleOpt.get().addMediaContent(mediaContent);
                        this.mediaContentRepository.save(mediaContent);
                        this.propertySaleService.editPropertySale(existPropertySaleOpt.get());

                        Optional<MediaContent> savedMediaContent = this.mediaContentRepository.findById(mediaContent.getId());
                        return new ResponseEntity<>(savedMediaContent.get(), HttpStatus.CREATED);
                    }
                }else{
                    throw new PropertySaleNotFoundException(mediaContent.getPropertySale().getId());
                }

            //MediaContent for propertySale
            }else{
                Optional<PropertyRent> existPropertyRentOpt = this.propertyRentRepository.findById(mediaContent.getPropertyRent().getId());

                if(existPropertyRentOpt.isPresent()){
                    Example<MediaContent> mediaContentExample =  Example.of(mediaContent);
                    Optional<MediaContent> existMediaContent = this.mediaContentRepository.findOne(mediaContentExample);
                    if(existMediaContent.isPresent()){
                        throw new MediaContentAlreadyExistException();

                    }else{
                        existPropertyRentOpt.get().addMediaContent(mediaContent);
                        this.mediaContentRepository.save(mediaContent);
                        this.propertyRentService.editPropertyRent(existPropertyRentOpt.get());

                        Optional<MediaContent> savedMediaContent = this.mediaContentRepository.findById(mediaContent.getId());
                        return new ResponseEntity<>(savedMediaContent.get(), HttpStatus.CREATED);
                    }
                }else{
                    throw new PropertyRentNotFoundException(mediaContent.getPropertyRent().getId());
                }
            }
        }
    }

    @Override
    public ResponseEntity<MediaContent> editMediaContent(MediaContent mediaContent) {
        Optional<MediaContent> oldMediaContentOpt = this.mediaContentRepository.findById(mediaContent.getId());
        if(oldMediaContentOpt.isPresent()){
            if(mediaContent.getPropertySale() == null && mediaContent.getPropertyRent() == null){
                throw new MediaContentParameterException();

            }else if(mediaContent.getPropertySale() != null && mediaContent.getPropertyRent() != null) {
                throw new MediaContentParameterException();

            }else{
                MediaContent savedMediaContent = this.mediaContentRepository.save(mediaContent);
                return new ResponseEntity<>(savedMediaContent, HttpStatus.CREATED);
            }

        }else{
            throw new MediaContentNotFoundException(mediaContent.getId());
        }
    }

    @Override
    public ResponseEntity<MediaContent> deleteMediaContent(MediaContent mediaContent) {
        if(mediaContent.getPropertySale() == null && mediaContent.getPropertyRent() == null){
            throw new MediaContentParameterException();

        }else if(mediaContent.getPropertySale() != null && mediaContent.getPropertyRent() != null) {
            throw new MediaContentParameterException();

        }else{
            Optional<MediaContent> oldMediaContentOpt = this.mediaContentRepository.findById(mediaContent.getId());

            if(oldMediaContentOpt.isPresent()){
                //If the mediaContent is for propertyRent
                if(mediaContent.getPropertyRent() != null){
                    Optional<PropertyRent> oldPropertyRentOpt = this.propertyRentRepository.findById(oldMediaContentOpt.get().getPropertyRent().getId());
                    oldPropertyRentOpt.get().deleteMediaContent(oldMediaContentOpt.get());
                    this.propertyRentService.editPropertyRent(oldPropertyRentOpt.get());

                //If the mediaContent is for propertySale
                }else{
                    Optional<PropertySale> oldPropertySaleOpt = this.propertySaleRepository.findById(oldMediaContentOpt.get().getPropertySale().getId());
                    oldPropertySaleOpt.get().deleteMediaContent(oldMediaContentOpt.get());
                    this.propertySaleService.editPropertySale(oldPropertySaleOpt.get());
                }

                this.mediaContentRepository.deleteById(mediaContent.getId());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            }else{
                throw new MediaContentNotFoundException(mediaContent.getId());
            }
        }
    }

    @Override
    public ResponseEntity<MediaContent> deleteAllMediaContent() {
        List<MediaContent> mediaContentList = this.mediaContentRepository.findAll();

        for(MediaContent mediaContent : mediaContentList){
            deleteMediaContent(mediaContent);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<MediaContent>> searchMediaContent(MediaContent mediaContent) {
        Example<MediaContent> saleMediaContent =  Example.of(mediaContent);
        List<MediaContent> mediaList = this.mediaContentRepository.findAll(saleMediaContent);

        if(mediaList.size() == 0){
            throw new NoResultFoundException();
        }else {
            return new ResponseEntity<>(mediaList, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<List<MediaContent>> getAllMediaContent() {
        List<MediaContent> mediaContentList = this.mediaContentRepository.findAll();

        if(mediaContentList.size() == 0){
            throw new DatabaseIsEmptyException();
        }else{
            return new ResponseEntity<>(mediaContentList, HttpStatus.OK);
        }
    }
}
