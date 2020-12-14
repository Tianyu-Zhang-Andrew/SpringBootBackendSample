package com.mikason.PropView.services.documentService;

import com.mikason.PropView.dataaccess.documentEntity.MediaContent;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MediaContentService {
    ResponseEntity<MediaContent> saveMediaContent(MediaContent mediaContent);
    ResponseEntity<MediaContent> editMediaContent(MediaContent mediaContent);
    ResponseEntity<MediaContent> deleteMediaContent(MediaContent mediaContent);
    ResponseEntity<MediaContent> deleteAllMediaContent();
    ResponseEntity<List<MediaContent>> searchMediaContent(MediaContent mediaContent);
    ResponseEntity<List<MediaContent>> getAllMediaContent();
}
