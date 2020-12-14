package com.mikason.PropView.controller;

import com.mikason.PropView.dataaccess.documentEntity.MediaContent;
import com.mikason.PropView.services.documentService.MediaContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MediaContentController {
    @Autowired
    private MediaContentService mediaContentService;

    @PostMapping("/mediaContent/saveMediaContent")
    ResponseEntity<MediaContent> saveMediaContent(@RequestBody MediaContent mediaContent){
        return mediaContentService.saveMediaContent(mediaContent);
    }

    @PostMapping("/mediaContent/editMediaContent")
    ResponseEntity<MediaContent> editMediaContent(@RequestBody MediaContent mediaContent){
        return mediaContentService.editMediaContent(mediaContent);
    }

    @PostMapping("/mediaContent/searchMediaContent")
    ResponseEntity<List<MediaContent>> searchMediaContent(@RequestBody MediaContent mediaContent){
        return mediaContentService.searchMediaContent(mediaContent);
    }

    @PostMapping("/mediaContent/deleteMediaContent")
    ResponseEntity<MediaContent> deleteMediaContent(@RequestBody MediaContent mediaContent){
        return mediaContentService.deleteMediaContent(mediaContent);
    }

    @PostMapping("/mediaContent/deleteAllMediaContent")
    ResponseEntity<MediaContent> deleteAllMediaContent(){
        return mediaContentService.deleteAllMediaContent();
    }

    @GetMapping("/mediaContent/getAllMediaContent")
    ResponseEntity<List<MediaContent>> getAllMediaContent(){
        return mediaContentService.getAllMediaContent();
    }
}
