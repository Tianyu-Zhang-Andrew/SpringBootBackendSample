package com.mikason.PropView.Exception.documentEntityException;

public class MediaContentParameterException extends RuntimeException {
    public MediaContentParameterException() {
        super ("One of propertyRent and propertySale for mediaContent must be null and the other is not");
    }
}
