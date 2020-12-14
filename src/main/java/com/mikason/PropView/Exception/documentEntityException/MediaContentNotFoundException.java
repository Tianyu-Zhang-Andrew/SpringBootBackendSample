package com.mikason.PropView.Exception.documentEntityException;

public class MediaContentNotFoundException extends RuntimeException {
    public MediaContentNotFoundException(Long id) {
        super ("Could not find media content " + id);
    }
}
