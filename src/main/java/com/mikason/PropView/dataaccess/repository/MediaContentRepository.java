package com.mikason.PropView.dataaccess.repository;

import com.mikason.PropView.dataaccess.documentEntity.MediaContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaContentRepository extends JpaRepository<MediaContent, Long> {
}
