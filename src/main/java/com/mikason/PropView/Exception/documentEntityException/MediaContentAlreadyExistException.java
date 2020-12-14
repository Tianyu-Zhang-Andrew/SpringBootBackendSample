package com.mikason.PropView.Exception.documentEntityException;

public class MediaContentAlreadyExistException extends RuntimeException{
    public MediaContentAlreadyExistException() {
        super ("Media content already exist, fail to create Media content");
    }
}
